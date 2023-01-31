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
import com.vaticle.dependencies.library.util.get_host_arch
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.forEachDirectoryEntry
import kotlin.io.path.notExists

fun main(args: Array<String>) {
    val zipDest = args.get(0);
    val baseDir = Paths.get(".")
    val version = Paths.get("library").resolve("dll").resolve("rocksdb").resolve("VERSION").toFile().useLines { it.firstOrNull() }
    val envVars: Map<String, String> = mapOf();
    val rocksDir = Paths.get("rocksdb")

    if (baseDir.resolve(rocksDir).notExists()) {
        bash("git clone https://github.com/facebook/rocksdb.git", baseDir, envVars, true)
    }

    bash("git checkout v$version", rocksDir, envVars, true)

    bash("brew install cmake", rocksDir, envVars, false)
    bash("make clean jclean", rocksDir, envVars, true)

    val hostArch = get_host_arch(rocksDir)
    if (hostArch == "arm64") {
        make_arm64_host(rocksDir);
    } else if (hostArch == "x86_64") {
        make_x86_64_host(rocksDir);
    } else throw RuntimeException("Unrecognized architecture '$hostArch'");

    var rocksLibs = "";
    rocksDir.forEachDirectoryEntry(glob = "librocksdb*.dylib") {
        rocksLibs += "${it.fileName} "
    }
    bash("zip librocksdb.zip ${rocksLibs.trim()}", rocksDir, envVars, true)
    bash("mv ${rocksDir.resolve("librocksdb.zip")} $zipDest", baseDir, envVars, true);
}

fun make_arm64_host(rocksDir: Path) {
    val makeVars = mapOf("ARCHFLAG" to "-arch arm64")
    bash("arch -arm64 make shared_lib -j", rocksDir, makeVars, true)
}

fun make_x86_64_host(rocksDir: Path) {
    val makeVars = mapOf("TARGET_ARCHITECTURE" to "arm64")
    bash("arch -x86_64 make shared_lib -j 2", rocksDir, makeVars, true);
}