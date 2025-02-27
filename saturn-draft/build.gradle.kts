plugins {
    kotlin("jvm")
    java
    application
    id ("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "org.ldemetrios"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://www.jetbrains.com/intellij-repository/releases/")
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.ldemetrios:tyko:0.4.0")
    implementation("org.jetbrains.kotlin:kotlin-scripting-common:2.1.0")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm:2.1.0")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host:2.1.0")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jsr223:2.1.0")
    implementation("org.jetbrains.kotlin:kotlin-scripting-dependencies:2.1.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

sourceSets.main {
    java.srcDir("src")
}

sourceSets.test {
    java.srcDir("test")
}

//tasks.shadowJar {
//    manifest {
//        attributes(
//            "Saturn-Export" to "org.ldemetrios.saturn.example.MainKt",
//        )
//    }
//}

application {
    mainClass.set("org.ldemetrios.saturnDraft.MainKt")
}