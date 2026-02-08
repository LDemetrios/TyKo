plugins {
    kotlin("jvm")
}

group = "org.ldemetrios"
version = "0.4.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core:5.8.1")
    testImplementation("org.jsoup:jsoup:1.17.2")
    testImplementation(project(":runtime"))
    testImplementation(project(":compiler"))
    testImplementation(project(":drivers:chicory"))
    testImplementation(project(":model"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks.named<Test>("test") {
    maxHeapSize = "8G"
}
