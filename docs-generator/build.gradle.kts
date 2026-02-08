plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "8.3.0"
    application
}

group = "org.ldemetrios"
version = "0.4.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jsoup:jsoup:1.17.2")
}

application {
    mainClass = "org.ldemetrios.docgen.org.ldemetrios.tyko.docs.MainKt"
}

tasks.test {
    useJUnitPlatform()
}
