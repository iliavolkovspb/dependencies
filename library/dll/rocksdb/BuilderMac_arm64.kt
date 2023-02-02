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
import java.nio.file.Path
import java.nio.file.Paths

fun main(args: Array<String>) {
    if (args.size != 2) throw RuntimeException("Expected 2 arguments: <version> <outDir>")
    val version = args[0]
    val outFile = Paths.get(args[1])
    if (!outFile.endsWith(".zip")) throw RuntimeException("Expected out file to be a .zip")
    val buildDir = buildMac(version, ::makeHost_arm64, ::makeHost_x86_64)
    validateFileDescription(buildDir, "librocksdb.dylib", "arm")
    val zipName = "librocksdb.zip"
    createZip(zipName, buildDir, "librocksdb*.dylib")
    bash("mv ${buildDir.resolve(zipName)} $outFile", buildDir, mapOf(), true)
}

private fun makeHost_arm64(rocksDir: Path) {
    val makeVars = mapOf(
            "ARCHFLAG" to "-arch arm64",
            "DISABLE_WARNING_AS_ERROR" to "true",
    )
    bash("arch -arm64 make shared_lib -j", rocksDir, makeVars, true)
}

private fun makeHost_x86_64(rocksDir: Path) {
    val makeVars = mapOf(
            "ARCHFLAG" to "-arch arm64",
            "EXTRA_LDFLAGS" to "-target arm64-apple-darwin",
            "DISABLE_WARNING_AS_ERROR" to "true"
    )
    // note: over-parallelising can cause nontermination in CircleCI builds
    bash("arch -x86_64 make shared_lib -j 2", rocksDir, makeVars, true)
}