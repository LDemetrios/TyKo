package org.ldemetrios.typst4k

import org.ldemetrios.typst4k.dsl.Typst
import org.ldemetrios.typst4k.dsl.compile
import org.ldemetrios.typst4k.model.*
import org.ldemetrios.typst4k.rt.*
import java.io.File
import java.nio.file.Path

fun main() {
    val content = TSequence(
        THeading(body = "Methods".text, depth = 1.t),
        TSpace(),
        "We follow the glacier melting models established earlier.".text,
        TParbreak(),
        TCustomContent("lorem", listOf(15.t), mapOf()),
        TParbreak(),
        "Total displaced soil by glacial flow:".text,
        TParbreak(),
        TCustomContent(
            "eval",
            listOf("\$ 7.32 beta + sum_(i=0)^nabla (Q_i (a_i - epsilon)) / 2 \$".t),
            mapOf("mode" to "markup".t)
        )
    )
    Typst("./typst").compile("#set page(height:auto)\n #" + content.repr(), Path.of("example.png"))
}
