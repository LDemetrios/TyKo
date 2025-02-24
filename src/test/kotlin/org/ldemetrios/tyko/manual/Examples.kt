package org.ldemetrios.tyko.manual

import org.ldemetrios.tyko.compiler.*
import org.ldemetrios.tyko.ffi.TypstSharedLibrary
import org.ldemetrios.tyko.model.*
import org.ldemetrios.tyko.operations.*
import java.io.File
import kotlin.io.path.Path

fun main() {
    val tiling = TTiling(
        sz = TArray(30.pt, 30.pt),
        body = TSequence(
            TPlace(body = TLine(start = TArray(0.pc, 0.pc), end = TArray(100.pc, 100.pc))),
            TPlace(body = TLine(start = TArray(0.pc, 100.pc), end = TArray(100.pc, 0.pc))),
        )
    )

    val lib = TypstSharedLibrary.instance(Path("../libtypst_shared.so"))

    val compiler = WorldBasedTypstCompiler(lib, DetachedWorld())

    val anotherTiling = compiler.evalDetached(
        """
            tiling(
                size: (30pt, 30pt),
                spacing: (10pt, 10pt),
                relative: "parent",
                square(
                    size: 30pt,
                    fill: gradient.conic(..color.map.rainbow),
                ),
            )
        """.trimIndent()
    )

    compiler.close()

    val page = TSequence {
        +TSetPage(height = TAuto, margin = 20.pt)
        +TRect(
            fill = tiling,
            width = 100.pc,
            height = 60.pt,
            stroke = 1.pt,
        )
    }
    val fileCompiler = WorldBasedTypstCompiler(lib, SingleFileWorld("#" + page.repr()))

    val compiled = fileCompiler.compilePng(ppi = 288.0f)[0] // Oth page
    File("output.png").writeBytes(compiled)
}