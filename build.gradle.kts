import newborn.main
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.0"
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.0"
    kotlin("plugin.serialization") version "2.1.0"
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
    implementation("org.ldemetrios:common-utils:0.1.3")
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

tasks.register("generateModel") {
    group = "build"
    description = "Generates model classes from others"
    doLast {
        main(
//            datamodelFile = "$rootDir/others",
//            prefix = "T",
//            commonInterfaceName = "TValue",
//            location = "$rootDir/src/main/kotlin/org/ldemetrios/tyko/orm",
//            packageName = "org.ldemetrios.tyko.orm",
        )
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        // General options for main source sets, no special flags needed for context receivers
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().matching {
    it.name.contains("Test") // Only target KotlinCompile tasks for test sources
}.configureEach {
    kotlinOptions {
        freeCompilerArgs += "-Xcontext-receivers"
    }
}

