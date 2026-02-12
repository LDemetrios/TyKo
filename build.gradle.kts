//import newborn.main
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.0"
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.0"
    kotlin("plugin.serialization") version "2.3.0"
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

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    testImplementation("io.kotest:kotest-runner-junit5:5.7.0")
    testImplementation("io.kotest:kotest-assertions-core:5.7.0")
    testImplementation("io.kotest:kotest-property:5.7.0")
    implementation("net.java.dev.jna:jna:5.13.0")
    implementation("net.java.dev.jna:jna-platform:5.13.0")
    implementation("org.apache.commons:commons-compress:1.26.0")

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

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
    this.repositories {
        mavenLocal()
    }
}

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

//tasks.register("generateModel") {
//    group = "build"
//    description = "Generates model classes from others"
//    doLast {
//        main(
////            datamodelFile = "$rootDir/others",
////            prefix = "T",
////            commonInterfaceName = "TValue",
////            location = "$rootDir/src/main/kotlin/org/ldemetrios/tyko/orm",
////            packageName = "org.ldemetrios.tyko.orm",
//        )
//    }
//}

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
        apply(plugin = "maven-publish")

        var publicationConfigured = false
        fun configureMavenPublicationOnce() {
            if (publicationConfigured) return
            publicationConfigured = true
            publishing {
                publications {
                    if (findByName("maven") == null) {
                        create<MavenPublication>("maven") {
                            from(components["java"])
                            artifactId = ("tyko" + project.path.split(":")
                                .filter { it.isNotBlank() }
                                .joinToString("-", prefix = "-"))
                        }
                    }
                }
                repositories {
                    mavenLocal()
                }
            }
        }

        plugins.withId("org.jetbrains.kotlin.jvm") {
            configureMavenPublicationOnce()
        }
        plugins.withId("java") {
            configureMavenPublicationOnce()
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
