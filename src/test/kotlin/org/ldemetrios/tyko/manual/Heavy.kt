package org.ldemetrios.tyko.manual

import org.ldemetrios.tyko.compiler.*
import org.ldemetrios.tyko.ffi.TypstSharedLibrary
import org.ldemetrios.tyko.model.TDictionary
import org.ldemetrios.tyko.model.TEmptyDictionaryImpl
import org.ldemetrios.tyko.model.TValue
import java.time.Instant
import kotlin.io.path.Path

val src = """
= Total Store Order

#import "@preview/fletcher:0.5.5" as fletcher: diagram as dia, node, edge

#let diagram(..args) = align(center)[
  \
  #context dia(
    edge-stroke: 1pt + text.fill,
    node-corner-radius: 5pt,
    edge-corner-radius: 8pt,
    mark-scale: 80%,
    node-fill: eastern,
    ..args,
  )
  \
]

#diagram(
  node((0, 0), [#h(2em) CPU 1 #h(2em)], name:<cpu1>),
  node((.8, 0), [#h(2em) CPU 2 #h(2em)], name:<cpu2>),
  node((0.15, 1), [buffer], name:<buf1>),
  node((0.65, 1), [buffer], name:<buf2>),
  node((0.4, 2), [#h(6em) Memory 2 #h(6em)], name:<mem>),

  edge(<cpu1>, "dd", "<{-", shift: -.5, [read]),
  edge(<cpu2>, "dd", "<{-", shift: 0.2, [read]),

  edge(<buf1>, "u", "<{-", [write]),
  edge(<buf2>, "u", "<{-", [write]),
  edge(<buf1>, "d", "-}>"),
  edge(<buf2>, "d", "-}>", [write back]),
)

""".trimIndent()

class HeavyWorld() : World {
    override fun library(): StdlibProvider = object : StdlibProvider.Standard {
        override val inputs: TDictionary<TValue> get() = TEmptyDictionaryImpl
        override val features: List<Feature> get() = listOf(Feature.Html)
    }

    override fun mainFile(): FileDescriptor {
        return FileDescriptor(null, "/main.typ")
    }

    override fun file(file: FileDescriptor): RResult<ByteArray, FileError> {
        println(file)
        return when (file.pack?.namespace) {
            null -> {
                if ("/main.typ" == file.path) RResult.Ok(src.toByteArray())
                else RResult.Err(FileError.NotFound(file.path))

            }

            else -> TODO()
        }.also{println("returned")}
    }

    override fun now(): WorldTime? = WorldTime.Fixed(Instant.EPOCH)

    override val autoManageCentral: Boolean = true
}


fun main() {
    val compiler = WorldBasedTypstCompiler(
        TypstSharedLibrary.instance(Path("/home/ldemetrios/Workspace/TypstNKotlinInterop/libtypst_shared.so")),
        HeavyWorld()
    )
    println(compiler.compileSvg(0, Int.MAX_VALUE))
}