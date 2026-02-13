package org.ldemetrios.tyko.driver.chicory_based.buildtime

import com.dylibso.chicory.build.time.compiler.Config
import com.dylibso.chicory.build.time.compiler.Generator
import com.dylibso.chicory.compiler.InterpreterFallback
import com.dylibso.chicory.wasm.Parser
import com.dylibso.chicory.wasm.types.NameCustomSection
import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.Path

object ChicoryBuildTimeCompiler {
    private const val FUNCTION_NAMES_RESOURCE = "typst-shared-function-names.txt"

    @JvmStatic
    fun main(args: Array<String>) {
        require(args.size == 4) {
            "Usage: ChicoryBuildTimeCompiler <wasmPath> <generatedSourceDir> <generatedResourcesDir> <generatedClassesDir>"
        }
        val wasmPath = Path.of(args[0])
        val sourceDir = Path.of(args[1])
        val resourcesDir = Path.of(args[2])
        val classesDir = Path.of(args[3])
        val config = Config.builder()
            .withWasmFile(wasmPath)
            .withName("org.ldemetrios.tyko.driver.chicory_based.TypstShared")
            .withTargetClassFolder(classesDir)
            .withTargetSourceFolder(sourceDir)
            .withTargetWasmFolder(resourcesDir)
            .withInterpreterFallback(InterpreterFallback.WARN)
            .build()
        val generator = Generator(config)
        val interpreted = generator.generateResources()
        generator.generateMetaWasm(interpreted)
        generator.generateSources()
        writeFunctionNames(wasmPath, resourcesDir)
    }

    private fun writeFunctionNames(wasmPath: Path, resourcesDir: Path) {
        val module = Parser.parse(wasmPath)
        val nameSection = module.customSection("name") as? NameCustomSection
        val importCount = module.importSection().importCount()
        val totalFunctions = importCount + module.functionSection().functionCount()
        val outputPath = resourcesDir.resolve(FUNCTION_NAMES_RESOURCE)
        Files.createDirectories(resourcesDir)
        BufferedWriter(Files.newBufferedWriter(outputPath)).use { writer ->
            if (nameSection != null) {
                for (idx in 0 until totalFunctions) {
                    val rawName = nameSection.nameOfFunction(idx) ?: continue
                    val demangled = try {
                        Demangler.demangle(rawName)
                    } catch (_: Throwable) {
                        rawName
                    }
                    writer.append(idx.toString())
                    writer.append('\t')
                    writer.append(demangled)
                    writer.newLine()
                }
            }
        }
    }
}
