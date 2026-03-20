plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "8.3.0"
}

group = "org.ldemetrios"
version = "0.5.a"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":model"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
