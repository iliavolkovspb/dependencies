/*
 *  Copyright (C) 2022 Vaticle
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.vaticle.dependencies.library.dll.rocksdb

import com.vaticle.dependencies.library.util.bash
import com.vaticle.dependencies.library.util.getHostArch
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.forEachDirectoryEntry
import kotlin.io.path.notExists

fun main(args: Array<String>) {
    val versionFile = Paths.get(args[0]);
    if (versionFile.notExists()) throw RuntimeException("Version file not found '$versionFile'");
    val zipDest = args[1];
    val version = versionFile.toFile().useLines { it.firstOrNull() }
    val envVars: Map<String, String> = mapOf();
    val baseDir = Paths.get(".")

    val rocksDir = baseDir.resolve("rocksdb")
    if (rocksDir.notExists()) {
        bash("git clone https://github.com/facebook/rocksdb.git", baseDir, envVars, true)
    }
    bash("git checkout v$version", rocksDir, envVars, true)

    bash("pwd", rocksDir, envVars, true);

    bash("brew install cmake", rocksDir, envVars, false)
    bash("make clean jclean", rocksDir, envVars, true)

    val hostArch = getHostArch()
    if (hostArch == "arm64") {
        makeArm64Host(rocksDir);
    } else if (hostArch == "x86_64") {
        makeX86_64Host(rocksDir);
    } else throw RuntimeException("Unrecognized architecture '$hostArch'");

    var rocksLibs = "";
    rocksDir.forEachDirectoryEntry(glob = "librocksdb*.dylib") {
        rocksLibs += "${it.fileName} "
    }
    bash("zip librocksdb.zip ${rocksLibs.trim()}", rocksDir, envVars, true)
    bash("mv ${rocksDir.resolve("librocksdb.zip")} $zipDest", baseDir, envVars, true);
}

fun makeArm64Host(rocksDir: Path) {
    val makeVars = mapOf(
            "ARCHFLAG" to "-arch arm64",
            "DISABLE_WARNING_AS_ERROR" to "true",
    )
    bash("arch -arm64 make shared_lib -j", rocksDir, makeVars, true)
}

fun makeX86_64Host(rocksDir: Path) {
    val makeVars = mapOf(
            "ARCHFLAG" to "-arch arm64",
            "DISABLE_WARNING_AS_ERROR" to "true",
            "EXTRA_LDFLAGS" to "-target arm64-apple-darwin"
    )
    bash("arch -x86_64 make shared_lib -j 2", rocksDir, makeVars, true);
}