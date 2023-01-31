package com.vaticle.dependencies.library.rocksdbjni

import com.vaticle.dependencies.library.util.bash
import java.nio.file.Paths

fun main() {
    val baseDir = Paths.get(".")
    val version = Paths.get("library").resolve("rocksdbjni").resolve("VERSION").toFile().useLines { it.firstOrNull() }

    val javaHome = Paths.get(bash("/usr/libexec/java_home", baseDir, mapOf(), true).outputUTF8().trim())
    val envVars = mapOf("JAVA_HOME" to javaHome.toAbsolutePath().toString());

    bash("git clone https://github.com/facebook/rocksdb.git", baseDir, envVars, true)

    val rocksDbDir = Paths.get("rocksdb")
    bash("git checkout v$version", rocksDbDir, envVars, true)

    bash("brew install cmake", rocksDbDir, envVars, false)

    bash("make clean jclean", rocksDbDir, envVars, true)

    // use 'make DEBUG_LEVEL=0 ...' to build production binary
    bash("make -j8 rocksdbjava", rocksDbDir, envVars, true)

    val srcMainJavaDir = Paths.get("rocksdb", "java", "src", "main", "java")
    val sourcesJarName = "rocksdbjni-$version-sources.jar"
    bash("jar -cf ../../../target/$sourcesJarName org", srcMainJavaDir, envVars, true)

    val versionedJarName = "rocksdbjni-$version-osx.jar"
    val jar = rocksDbDir.resolve("java").resolve("target").resolve(versionedJarName).toFile()
    val destPath = Paths.get("rocksdbjni-osx.jar").toFile()
    jar.copyTo(destPath)

    val sourcesJar = rocksDbDir.resolve("java").resolve("target").resolve(sourcesJarName).toFile()
    val sourcesDestPath = Paths.get("rocksdbjni-sources.jar").toFile()
    sourcesJar.copyTo(sourcesDestPath)
}