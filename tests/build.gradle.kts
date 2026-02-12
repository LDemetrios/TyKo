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
    implementation("io.kotest:kotest-runner-junit5:5.8.1")
    implementation("io.kotest:kotest-assertions-core:5.8.1")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("org.apache.commons:commons-compress:1.26.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation(project(":runtime"))
    implementation(project(":compiler"))
    implementation(project(":drivers:chicory"))
    implementation(project(":model"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.named<Test>("test") {
    maxHeapSize = "8G"
}