plugins {
    kotlin("jvm")
}

group = "org.ldemetrios"
version = "0.4.0"

repositories {
    mavenCentral()
}

dependencies {
    api("io.kotest:kotest-runner-junit5:5.8.1")
    api("io.kotest:kotest-assertions-core:5.8.1")
    api("org.jsoup:jsoup:1.17.2")
    api(project(":runtime"))
}

tasks.test {
    useJUnitPlatform()
}