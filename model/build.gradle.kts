plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "org.ldemetrios"
version = "0.4.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar>().configureEach {
    from(rootProject.file("LICENSE.txt"))
    from(rootProject.file("NOTICE"))
}

kotlin {
    jvmToolchain(17)
}
