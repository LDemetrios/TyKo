package org.ldemetrios.tyko.tests

import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import org.ldemetrios.tyko.compiler.*
import org.ldemetrios.tyko.driver.chicory.ChicoryTypstCore
import org.ldemetrios.tyko.driver.chicory.defaultWasiOptions
import org.ldemetrios.tyko.runtime.TypstRuntime

class PackageCentralLoadWork : FreeSpec({
    "Compiles with preview package via fs context download" {
        val runtime = TypstRuntime(
            ChicoryTypstCore(
                defaultWasiOptions()
            )
        )
        val main = FileDescriptor(null, "/main.typ")
        val source = """
            #import "@preview/alchemist:0.1.8" : *
            = Hello
        """.trimIndent()

        val context = runtime.fileContext { file ->
            when (val pack = file.packageSpec?.namespace) {
                null -> if (file.virtualPath == "/main.typ") {
                    RResult.Ok(Base64Bytes(source.toByteArray()))
                } else {
                    RResult.Err(FileError.NotFound(file.virtualPath))
                }

                "preview" -> runtime.resolvePreviewPackage(file)
                else -> RResult.Err(FileError.Package(PackageError.NotFound(file.packageSpec!!)))
            }
        }

        val result = runtime.evalMainRaw(context, main)
        when (val output = result.output) {
            is RResult.Ok -> println(output.value)
            is RResult.Err -> fail("Expected package load to succeed, got: ${output.error}")
        }

        context.close()
        runtime.close()
    }
})
