//import newborn.main
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.0"
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.0"
    kotlin("plugin.serialization") version "2.3.0"
    id("com.vanniktech.maven.publish") version "0.36.0" apply false
}


group = "org.ldemetrios"
version = "0.4.0"
//
//tasks.jar {
//    manifest {
//        attributes["Class-Path"] = "/libs/a.jar"
//    }
//}

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    maven {
        url = uri("https://www.jetbrains.com/intellij-repository/releases/")
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

//val datamodel = File("${project.rootDir}/others").readText()

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

//tasks.withType<KotlinCompile> {
//    dependsOn("generateModel")
//}

tasks.test {
    useJUnitPlatform()
}

subprojects {
    val excludedFromPublishing = setOf(
        "drivers",
        "docs-generator"
    )

    // Ensure published metadata targets Java 17 for all published subprojects
    plugins.withId("org.jetbrains.kotlin.jvm") {
        kotlin {
            jvmToolchain(17)
        }
    }
    plugins.withId("java") {
        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }

    if (!path.startsWith(":tests") && name !in excludedFromPublishing) {
        apply(plugin = "com.vanniktech.maven.publish")
        apply(plugin = "signing")

        configure<SigningExtension> {
            val signingKeyFile: String? by project
            val signingPassword: String? by project
            val key = signingKeyFile?.let { File(it).readText() }
            useInMemoryPgpKeys(key, signingPassword)
        }

        extensions.configure<MavenPublishBaseExtension>("mavenPublishing") {
            publishToMavenCentral()
            signAllPublications()

            coordinates(
                "org.ldemetrios",
                "tyko" + project.path.split(":")
                    .filter { it.isNotBlank() }
                    .joinToString("-", prefix = "-"),
                "0.4.0"
            )

            pom {
                name.set("TyKo")
                description.set("Kotlin bindings for Typst")
                url.set("https://github.com/LDemetrios/TyKo")

                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("LDemetrios")
                        name.set("Dmitry Roman Liapin")
                        email.set("lyadmitry@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/LDemetrios/TyKo")
                    connection.set("scm:git:git://github.com/LDemetrios/TyKo.git")
                    developerConnection.set("scm:git:ssh://git@github.com/LDemetrios/TyKo.git")
                }
            }
        }
    }

    tasks.withType<KotlinCompile>().matching {
        it.name.contains("Test") // Only target KotlinCompile tasks for test sources
    }.configureEach {
        compilerOptions {
            freeCompilerArgs.add("-Xcontext-parameters")
        }
    }
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.add("-Xcontext-parameters")
            freeCompilerArgs.add("-Xannotation-target-all")
        }
    }
}

val centralPublishableProjects = subprojects.filter {
    !it.path.startsWith(":tests") && it.name !in setOf("drivers", "docs-generator")
}

tasks.register("publishToMavenCentral") {
    group = "publishing"
    description = "Publishes all publishable modules to Maven Central staging."
    dependsOn(centralPublishableProjects.map { "${it.path}:publishToMavenCentral" })
}

tasks.register("publishAndReleaseToMavenCentral") {
    group = "publishing"
    description = "Publishes and releases all publishable modules to Maven Central."
    dependsOn(centralPublishableProjects.map { "${it.path}:publishAndReleaseToMavenCentral" })
}

