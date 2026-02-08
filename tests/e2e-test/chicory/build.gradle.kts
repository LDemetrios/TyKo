plugins {
    kotlin("jvm")
}

group = "org.ldemetrios"
version = "0.4.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(project(":tests:e2e-test:common"))
    testImplementation(project(":drivers:chicory"))
}

tasks.test {
    useJUnitPlatform()
}