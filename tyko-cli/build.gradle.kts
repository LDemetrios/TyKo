plugins {
    kotlin("jvm")
    application
}

group = "org.ldemetrios"
version = "0.5.a"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":runtime"))
    implementation(project(":drivers:chicory-based"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("org.ldemetrios.MainKt")
}

kotlin {
    jvmToolchain(17)
}
