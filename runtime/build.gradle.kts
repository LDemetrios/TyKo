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
    api(project(":compiler"))
    api(project(":model"))
}

tasks.test {
    useJUnitPlatform()
}