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

import com.vaticle.dependencies.library.util.OS
import com.vaticle.dependencies.library.util.bash
import com.vaticle.dependencies.library.util.getOS
import com.vaticle.dependencies.library.util.getUnixHostArch
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.forEachDirectoryEntry
import kotlin.io.path.notExists

fun buildMac(gitVersion: String, makeHost_arm64: (Path) -> Unit, makeHost_x86_64: (Path) -> Unit): Path {
    if (getOS() != OS.MAC) throw RuntimeException("Mac builds only run on Mac operating systems.")
    val envVars: Map<String, String> = mapOf()
    val baseDir = Paths.get(".")
    val rocksDir = baseDir.resolve("rocksdb")
    checkoutRocksRepo(baseDir, rocksDir, gitVersion, envVars)
    installPrerequisites()
    bash("make clean jclean", rocksDir, envVars, true)

    val hostArch = getUnixHostArch()
    if (hostArch == "arm64") {
        makeHost_arm64(rocksDir)
    } else if (hostArch == "x86_64") {
        makeHost_x86_64(rocksDir)
    } else throw RuntimeException("Unrecognized architecture '$hostArch'")
    return rocksDir
}

private fun checkoutRocksRepo(baseDir: Path, rocksDir: Path, gitVersion: String, envVars: Map<String, String> = mapOf()) {
    if (rocksDir.notExists()) {
        bash("git clone https://github.com/facebook/rocksdb.git", baseDir, envVars, true)
    }
    bash("git checkout $gitVersion", rocksDir, envVars, true)
}

private fun installPrerequisites() {
    bash("brew install cmake", Paths.get("."), mapOf(), false)
}

fun validateFileDescription(fileDir: Path, fileName: String, expectedFileDescriptionSubstring: String) {
    val fileDescription = bash("file $fileName", fileDir, mapOf(), true ).outputUTF8().trim()
    if (!fileDescription.contains(expectedFileDescriptionSubstring)) {
        throw RuntimeException("File description for '$fileName' missing required '$expectedFileDescriptionSubstring': '$fileDescription'.")
    }
}

fun createZip(fileName: String, fileDir: Path, glob: String) {
    var files = "";
    fileDir.forEachDirectoryEntry(glob = glob) {
        files += "${it.fileName} "
    }
    bash("zip $fileName ${files.trim()}", fileDir, mapOf(), true)
}
