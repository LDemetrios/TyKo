import generator.kindaMain
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    `java-library`
    `maven-publish`
}

group = "org.ldemetrios"
version = "0.3.0"

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
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation(kotlin("reflect"))
    implementation("org.ldemetrios:common-utils:0.1.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    testImplementation("io.kotest:kotest-runner-junit5:5.7.0")
    testImplementation("io.kotest:kotest-assertions-core:5.7.0")
    testImplementation("io.kotest:kotest-property:5.7.0")
    implementation("com.jetbrains.intellij.java:java-decompiler-engine:242.22855.74")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
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
//        kindaMain(
//            datamodelFile = "$rootDir/others",
//            prefix = "T",
//            commonInterfaceName = "TValue",
//            location = "$rootDir/src/main/kotlin/org/ldemetrios/typst4k/orm",
//            packageName = "org.ldemetrios.typst4k.orm",
//        )
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

