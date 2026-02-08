import java.io.ByteArrayOutputStream
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "8.3.0"
}

group = "org.ldemetrios"
version = "0.4.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    api(project(":drivers:api"))
    implementation("com.dylibso.chicory:runtime:+")
    implementation("com.dylibso.chicory:compiler:+")
    api("com.dylibso.chicory:wasi:+")
}

tasks.test {
    useJUnitPlatform()
}

val debugBuild : Boolean = true

val buildTypstSharedLibrary = tasks.register("buildTypstSharedLibrary") {
    val cargoHome = System.getenv("CARGO_HOME") ?: "${System.getProperty("user.home")}/.cargo"
    val cargoBin = "$cargoHome/bin"
    val cargoExecutable = providers.environmentVariable("CARGO")
        .orElse(providers.gradleProperty("cargoPath"))
        .orElse("cargo")
    doLast {
        val stdout = ByteArrayOutputStream()
        val stderr = ByteArrayOutputStream()
        val resolvedCargo = cargoExecutable.get()
        val path = listOf(cargoBin, System.getenv("PATH")).joinToString(":")
        val cargoFile = if (resolvedCargo.contains(File.separator)) {
            file(resolvedCargo)
        } else {
            path.split(":")
                .filter { it.isNotBlank() }
                .map { file("$it/$resolvedCargo") }
                .firstOrNull { it.exists() }
        }
        if (cargoFile == null || !cargoFile.exists()) {
            throw GradleException(
                "cargo not found. Install Rust or set CARGO=/path/to/cargo or -PcargoPath=/path/to/cargo."
            )
        }
        val opensslDir = System.getenv("OPENSSL_DIR")?.takeIf { it.isNotBlank() } ?: run {
            val brewFile = path.split(":")
                .filter { it.isNotBlank() }
                .map { file("$it/brew") }
                .firstOrNull { it.exists() }
            if (brewFile != null) {
                val brewOut = ByteArrayOutputStream()
                val brewResult = project.exec {
                    executable = brewFile.absolutePath
                    args("--prefix", "openssl")
                    standardOutput = brewOut
                    errorOutput = ByteArrayOutputStream()
                    isIgnoreExitValue = true
                }
                if (brewResult.exitValue == 0) {
                    brewOut.toString().trim().ifEmpty { null }
                } else {
                    null
                }
            } else {
                null
            }
        }
        val result = project.exec {
            workingDir = rootProject.file("typst-shared-library")
            executable = cargoFile.absolutePath
            if (debugBuild) {
                args("build", "--target", "wasm32-wasip1")
            }else {
                args("build", "--target", "wasm32-wasip1", "--release")
            }
            environment("PATH", path)
            environment("RUSTFLAGS", listOf("-Awarnings", System.getenv("RUSTFLAGS") ?: "").joinToString(" ").trim())
            if (opensslDir != null) {
                environment("OPENSSL_DIR", opensslDir)
            }
            standardOutput = stdout
            errorOutput = stderr
            isIgnoreExitValue = true
        }
        val outText = stdout.toString().trim()
        val errText = stderr.toString().trim()
        if (outText.isNotEmpty()) {
            logger.lifecycle(outText)
        }
        if (errText.isNotEmpty()) {
            logger.error(errText)
        }
        if (result.exitValue != 0) {
            throw GradleException("cargo build failed with exit code ${result.exitValue}.")
        }
    }
}

val generatedWasmDir = layout.buildDirectory.dir("generated/resources/typst-shared-wasm")

val prepareTypstSharedWasm = tasks.register<Copy>("prepareTypstSharedWasm") {
    dependsOn(buildTypstSharedLibrary)
    val src = if (debugBuild ) {
        "typst-shared-library/target/wasm32-wasip1/debug/typst_shared.wasm"
    } else {
        "typst-shared-library/target/wasm32-wasip1/release/typst_shared.wasm"
    }
    from(rootProject.file(src)) {
        rename("typst_shared.wasm", "typst-shared.wasm")
    }
    into(generatedWasmDir)
}

sourceSets {
    main {
        resources.srcDir(generatedWasmDir)
    }
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(prepareTypstSharedWasm)
}

kotlin {
    jvmToolchain(21)
}
