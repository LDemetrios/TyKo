plugins {
    kotlin("jvm")
}

group = "org.ldemetrios"
version = "0.4.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    api(project(":compiler"))
    api(project(":model"))
}

tasks.test {
    useJUnitPlatform()
}