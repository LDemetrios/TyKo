plugins {
    kotlin("jvm")
}

group = "org.ldemetrios"
version = "0.5.a"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}