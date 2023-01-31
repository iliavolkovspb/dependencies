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

package com.vaticle.dependencies.library.util

import org.zeroturnaround.exec.ProcessExecutor
import org.zeroturnaround.exec.ProcessResult
import java.lang.RuntimeException
import java.nio.file.Path
import java.nio.file.Paths


fun bash(script: String, baseDir: Path, envVars: Map<String, String>, expectExitValueNormal: Boolean, quiet: Boolean = false): ProcessResult {
    return bash(script.split(" "), baseDir, envVars, expectExitValueNormal, quiet);
}

fun bash(script: List<String>, baseDir: Path, envVars: Map<String, String>, expectExitValueNormal: Boolean, quiet: Boolean = false): ProcessResult {
    if (!quiet) {
        println("======================================================================================")
        println("=== Running: '${script.joinToString(" ")}' ===")
        println("======================================================================================")
    }
    var builder = ProcessExecutor()
            .command(script)
            .readOutput(true)
            .redirectOutput(System.out)
            .redirectError(System.err)
            .directory(baseDir.toFile())
    for (entry in envVars) {
        builder = builder.environment(entry.key, entry.value)
    }
    if (expectExitValueNormal) {
        builder = builder.exitValueNormal()
    }
    val execution = builder.execute()
    if (!quiet) {
        println("======================================================================================")
        println("Execution finished with status code '${execution.exitValue}'")
        println("======================================================================================")
        println()
    }
    return execution
}

fun get_host_arch(rocksDir: Path): String {
    bash(listOf("arch"), rocksDir, mapOf(), false, false);


//    val translated = bash(listOf("/bin/sh", "-c", "sysctl sysctl.proc_translated"), rocksDir, mapOf(), false).outputUTF8().trim()
//    println(translated)
//    val isTranslatedProcessor = Integer.parseInt(translated.split(":")[1].trim()) == 1;
    val system = bash("uname -a", rocksDir, mapOf(), false, false).outputUTF8().trim();

    if (system.contains("arm64")) {
        return "arm64";
    } else return "x86_64";

//    if (system.contains("x86") || system.contains("amd64")) {
//    }
//        if (isTranslatedProcessor) return "arm64" else return "x86_64"
//    } else if (system.contains("arm64")) {
//        if (isTranslatedProcessor)  return "x86_64" else return "arm64"
//    } else throw RuntimeException("System unrecognised: must be x86/amd64 or arm64: $system");
//    return "arm64";
}
