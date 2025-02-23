package org.ldemetrios.tyko.manual

import org.ldemetrios.tyko.compiler.Feature
import org.ldemetrios.tyko.compiler.FileDescriptor
import org.ldemetrios.tyko.compiler.FileError
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.compiler.StdlibProvider
import org.ldemetrios.tyko.compiler.TypstCompilerException
import org.ldemetrios.tyko.compiler.World
import org.ldemetrios.tyko.compiler.WorldBasedTypstCompiler
import org.ldemetrios.tyko.compiler.WorldTime
import org.ldemetrios.tyko.compiler.compileSvg
import org.ldemetrios.tyko.ffi.TypstSharedLibrary
import org.ldemetrios.tyko.model.TDictionary
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.original.SHARED_LIBRARY_PATH
import kotlin.io.path.Path

fun tryCompile(source: String, lib: String? = null) {
    try {
        val world = object : World {
            override fun library(): StdlibProvider = object : StdlibProvider.Standard {
                override val inputs: TDictionary<TValue> get() = TDictionary(mapOf())
                override val features: List<Feature> get() = listOf()
            }

            override fun mainFile(): FileDescriptor = FileDescriptor(null, "/main.typ")

            override fun file(file: FileDescriptor): RResult<ByteArray, FileError> {
                return when {
                    file.pack == null && file.path == "/main.typ" -> RResult.Ok(source.trimIndent().encodeToByteArray())
                    file.pack == null && file.path == "/test-path/lib.typ" -> {
                        if (lib == null) RResult.Err(FileError.NotFound(file.path))
                        else RResult.Ok(lib.trimIndent().encodeToByteArray())
                    }

                    else -> RResult.Err(FileError.NotFound(file.path))
                }
            }

            override fun now(): WorldTime? = WorldTime.System
        }

        val compiler = WorldBasedTypstCompiler(
            TypstSharedLibrary.instance(Path(SHARED_LIBRARY_PATH)),
            world
        )
        compiler.compileSvg()
        compiler.close()
    } catch (e: TypstCompilerException) {
        e.printStackTrace()
    }
}

fun main() {
    tryCompile(
        """
            #let f(x) = x / 0
            #let g(x) = f(x) * 2
            #let h(x) = g(x)
            #h(3)
        """
    )

    tryCompile(
        """
            #let f(x) = 1 / 0
            #let g(x) = f(x) * 2
            #let h(x) = g(x)
            #show emph: it => h(it)
            This is _emphasized_
        """
    )


    tryCompile(
        """
            #import "/test-path/lib.typ"
            #let f(x) = lib.div(1, 0)
            #let g(x) = f(x) * 2
            #let h(x) = g(x)
            #show emph: it => h(it)
            This is _emphasized_
        """,
        """
            #let div(x, y) = x / y
        """.trimIndent()
    )
}