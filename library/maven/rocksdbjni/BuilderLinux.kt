package com.vaticle.dependencies.library.rocksdbjni

import java.nio.file.Paths

fun main() {
    try {
        val baseDir = Paths.get(".")
        val version = Paths.get("library").resolve("rocksdbjni").resolve("VERSION").toFile().useLines { it.firstOrNull() }

        val envVars = mapOf("JAVA_HOME" to Paths.get("/usr/lib/jvm/java-11-openjdk-amd64").toAbsolutePath().toString());

        com.vaticle.dependencies.library.util.bash("git clone https://github.com/facebook/rocksdb.git", baseDir, envVars, true)

        val rocksDbDir = Paths.get("rocksdb").toAbsolutePath()
        com.vaticle.dependencies.library.util.bash("git checkout v$version", rocksDbDir, envVars, true)

        com.vaticle.dependencies.library.util.bash("sudo apt install cmake", rocksDbDir, envVars, false)

        com.vaticle.dependencies.library.util.bash("make clean jclean", rocksDbDir, envVars, true)

        // use 'make DEBUG_LEVEL=0 ...' to build production binary
        com.vaticle.dependencies.library.util.bash("make -j8 rocksdbjava", rocksDbDir, envVars, true)

        println(">>>>>>>>>>>>>>>>>>>>> baseDir")
        com.vaticle.dependencies.library.util.bash("ls ${baseDir}", rocksDbDir, envVars, true)
        println(">>>>>>>>>>>>>>>>>>>>>")

        println(">>>>>>>>>>>>>>>>>>>>> rocksDbDir")
        com.vaticle.dependencies.library.util.bash("ls ${rocksDbDir}", rocksDbDir, envVars, true)
        println(">>>>>>>>>>>>>>>>>>>>>")

        println(">>>>>>>>>>>>>>>>>>>>> rocksDbDir.resolve(\"java\")")
        com.vaticle.dependencies.library.util.bash("ls ${rocksDbDir.resolve("java")}", rocksDbDir, envVars, true)
        println(">>>>>>>>>>>>>>>>>>>>>")

        println(">>>>>>>>>>>>>>>>>>>>> rocksDbDir.resolve(\"java\").resolve(\"target\")")
        com.vaticle.dependencies.library.util.bash("ls ${rocksDbDir.resolve("java").resolve("target")}", rocksDbDir, envVars, true)
        println(">>>>>>>>>>>>>>>>>>>>>")

        val versionedJarName = "rocksdbjni-$version-linux64.jar"
        println(">>>>>>>>>>>>>>>>>>>>>")

        println("1")
        val jar = rocksDbDir.resolve("java").resolve("target").resolve(versionedJarName).toFile()
        println("2")
        val destPath = Paths.get("rocksdbjni-linux.jar").toAbsolutePath().toFile()
        println("3")
        jar.copyTo(destPath)
        println("4")
        println(">>>>>>>>>>>>>>>>>>>>> destPath")
        com.vaticle.dependencies.library.util.bash("ls ${destPath.parent}", rocksDbDir, envVars, true)
        println(">>>>>>>>>>>>>>>>>>>>>")
    } catch (e: Throwable) {
        e.printStackTrace()
        throw e;
    }
}