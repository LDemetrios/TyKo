plugins {
    kotlin("jvm")
}

group = "org.ldemetrios"
version = "0.4.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.kotest:kotest-runner-junit5:5.8.1")
    implementation("io.kotest:kotest-assertions-core:5.8.1")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation(project(":runtime"))
    implementation(project(":compiler"))
    implementation(project(":drivers:chicory"))
    implementation(project(":model"))
}

tasks.test {
    useJUnitPlatform()
}