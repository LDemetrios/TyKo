import java.io.ByteArrayOutputStream
import org.gradle.language.jvm.tasks.ProcessResources
import at.released.wasm2class.InterpreterFallback
import at.released.wasm2class.Wasm2ClassExtension

plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "8.3.0"
    id("at.released.wasm2class.plugin") version "0.5.0"
}

group = "org.ldemetrios"
version = "0.4.0"

repositories {
    mavenCentral()
}

val buildTime by sourceSets.creating

dependencies {
    testImplementation(kotlin("test"))
    api(project(":drivers:api"))
    implementation("com.dylibso.chicory:runtime:1.6.1")
    api("com.dylibso.chicory:wasi:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    add("buildTimeImplementation", kotlin("stdlib"))
    add("buildTimeImplementation", "com.dylibso.chicory:build-time-compiler:1.6.1")
    add("buildTimeImplementation", "com.dylibso.chicory:runtime:1.6.1")
    add("chicoryCompiler", "com.dylibso.chicory:build-time-compiler:1.6.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar>().configureEach {
    from(rootProject.file("LICENSE.txt"))
    from(rootProject.file("NOTICE"))
}

val cargoProfile = providers.environmentVariable("TYKO_CARGO_PROFILE")
    .map { it.trim().lowercase() }
    .orElse("release")

val buildTypstSharedLibrary = tasks.register("buildTypstSharedLibrary") {
    val cargoHome = System.getenv("CARGO_HOME") ?: "${System.getProperty("user.home")}/.cargo"
    val cargoBin = "$cargoHome/bin"
    val cargoExecutable = providers.environmentVariable("CARGO")
        .orElse(providers.gradleProperty("cargoPath"))
        .orElse("cargo")
    doLast {
        val resolvedCargoProfile = cargoProfile.get()
        if (resolvedCargoProfile != "release" && resolvedCargoProfile != "debug") {
            throw GradleException(
                "Unsupported TYKO_CARGO_PROFILE='$resolvedCargoProfile'. Use 'release' or 'debug'."
            )
        }
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
            args(
                "build",
                "--target",
                "wasm32-wasip1",
                *if (resolvedCargoProfile == "release") arrayOf("--release") else emptyArray()
            )
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
    val src = cargoProfile.map { profile ->
        "typst-shared-library/target/wasm32-wasip1/$profile/typst_shared.wasm"
    }
    from(src.map { rootProject.file(it) }) {
        rename("typst_shared.wasm", "typst-shared.wasm")
    }
    into(generatedWasmDir)
}

tasks.named("precompileWasm2Class") { dependsOn(prepareTypstSharedWasm) }

configurations.named("chicoryCompiler") {
    resolutionStrategy.force("com.dylibso.chicory:build-time-compiler:1.6.1")
}

val wasm2classExt = extensions.getByType<Wasm2ClassExtension>()
val precompileWasm2ClassLargeHeap = tasks.register<JavaExec>("precompileWasm2ClassLargeHeap") {
    dependsOn(prepareTypstSharedWasm, "compileBuildTimeKotlin")
    classpath = sourceSets["buildTime"].runtimeClasspath
    mainClass.set("org.ldemetrios.tyko.driver.chicory_based.buildtime.ChicoryBuildTimeCompiler")
    jvmArgs("-Xms2g", "-Xmx8g")
    val wasmFile = generatedWasmDir.map { it.file("typst-shared.wasm") }
    inputs.file(wasmFile)
    outputs.dir(wasm2classExt.outputResources)
    outputs.dir(wasm2classExt.outputClasses)
    doFirst {
        wasm2classExt.outputSources.get().asFile.deleteRecursively()
        args = listOf(
            wasmFile.get().asFile.absolutePath,
            wasm2classExt.outputSources.get().asFile.absolutePath,
            wasm2classExt.outputResources.get().asFile.absolutePath,
            wasm2classExt.outputClasses.get().asFile.absolutePath
        )
    }
}

tasks.named("precompileWasm2Class") { enabled = false }

tasks.named("compileKotlin") { dependsOn(precompileWasm2ClassLargeHeap) }
tasks.named("compileJava") { dependsOn(precompileWasm2ClassLargeHeap) }
tasks.named<ProcessResources>("processResources") { dependsOn(precompileWasm2ClassLargeHeap) }

wasm2class {
    targetPackage.set("org.ldemetrios.tyko.driver.chicory_based")
    modules.create("typstShared") {
        wasm.set(generatedWasmDir.map { it.file("typst-shared.wasm") })
        interpreterFallback.set(InterpreterFallback.WARN)
    }
}

kotlin {
    jvmToolchain(17)
}
