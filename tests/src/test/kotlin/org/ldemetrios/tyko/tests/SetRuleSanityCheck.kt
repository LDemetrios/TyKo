package org.ldemetrios.tyko.tests

import io.kotest.core.spec.style.FreeSpec
import org.intellij.lang.annotations.Language
import org.ldemetrios.tyko.compiler.FSContextPointer
import org.ldemetrios.tyko.compiler.FileError
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.driver.chicory_based.ChicoryTypstCore
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.repr
import org.ldemetrios.tyko.runtime.TypstRuntime

class SetRuleSanityCheck : FreeSpec({
    // Check that nothing gone terribly wrong after set-rule refactoring

    val runtime = TypstRuntime { (ChicoryTypstCore()) }

    val additionalFiles = listOf(
        "american-chemical-society.csl",
        "works.bib",
    ).associate {
        "/$it" to String(
            SetRuleSanityCheck::class.java.classLoader.getResourceAsStream(it)!!.readAllBytes()
        )
    }

    fun checkCompiles(
        @Language("Typst") content: String
    ) {
        val ctx = runtime.fileContext(mapOf("/main.typ" to content) + additionalFiles)
        try {
            runtime.compilePng(ctx)
        } finally {
            ctx.release()
        }
    }

    fun checkCompiles(value: TValue) = checkCompiles("#" + value.repr { throw AssertionError() })

    "bibliography.style" {
        // path|str|bytes
        checkCompiles(
            """
                #set bibliography(style: "ieee")
                #bibliography("works.bib")
            """.trimIndent()
        )

        checkCompiles(
            """
                #set bibliography(style: "american-chemical-society.csl")
                #bibliography("works.bib")
            """.trimIndent()
        )

        checkCompiles(
            """
                #set bibliography(style: path("american-chemical-society.csl"))
                #bibliography("works.bib")
            """.trimIndent()
        )

        checkCompiles(
            """
                #set bibliography(style: bytes(read("american-chemical-society.csl")))
                #bibliography("works.bib")
            """.trimIndent()
        )

        // path|str|bytes
        checkCompiles(
            """
                #bibliography("works.bib", style: "ieee")
            """.trimIndent()
        )

        checkCompiles(
            """
                #bibliography("works.bib", style: "american-chemical-society.csl")
            """.trimIndent()
        )

        checkCompiles(
            """
                #bibliography("works.bib", style: path("american-chemical-society.csl"))
            """.trimIndent()
        )

        checkCompiles(
            """
                #bibliography("works.bib", style: bytes(read("american-chemical-society.csl")))
            """.trimIndent()
        )
    }

    "cite.style" {
        // auto|<same>
        @Language("typ") val code = """
            This was already noted by
            pirates long ago. @arrgh

            Multiple sources say ...
            @arrgh @netwok.

            You can also call `cite`
            explicitly. #cite(<arrgh>)

            #bibliography("works.bib")
        """.trimIndent()

        checkCompiles(
            """
                #set cite(style: auto)
                This was already noted by
                pirates long ago. @arrgh
    
                Multiple sources say ...
                @arrgh @netwok.
    
                You can also call `cite`
                explicitly. #cite(<arrgh>)
    
                #bibliography("works.bib")
            """.trimIndent()
        )

        checkCompiles(
            """
                #set cite(style: "ieee")
                This was already noted by
                pirates long ago. @arrgh
    
                Multiple sources say ...
                @arrgh @netwok.
    
                You can also call `cite`
                explicitly. #cite(<arrgh>)
    
                #bibliography("works.bib")
            """.trimIndent()
        )

        checkCompiles(
            """
                #set cite(style: "american-chemical-society.csl")
                This was already noted by
                pirates long ago. @arrgh
    
                Multiple sources say ...
                @arrgh @netwok.
    
                You can also call `cite`
                explicitly. #cite(<arrgh>)
    
                #bibliography("works.bib")
            """.trimIndent()
        )

        checkCompiles(
            """
                #set cite(style: path("american-chemical-society.csl"))
                This was already noted by
                pirates long ago. @arrgh
    
                Multiple sources say ...
                @arrgh @netwok.
    
                You can also call `cite`
                explicitly. #cite(<arrgh>)
    
                #bibliography("works.bib")
            """.trimIndent()
        )

        checkCompiles(
            """
                #set cite(style: bytes(read("american-chemical-society.csl")))
                This was already noted by
                pirates long ago. @arrgh
    
                Multiple sources say ...
                @arrgh @netwok.
    
                You can also call `cite`
                explicitly. #cite(<arrgh>)
    
                #bibliography("works.bib")
            """.trimIndent()
        )

        checkCompiles(
            """
                This was already noted by
                pirates long ago. @arrgh
    
                Multiple sources say ...
                @arrgh @netwok.
    
                You can also call `cite`
                explicitly. #cite(<arrgh>, style: auto)
    
                #bibliography("works.bib")
            """.trimIndent()
        )

        checkCompiles(
            """
                This was already noted by
                pirates long ago. @arrgh
    
                Multiple sources say ...
                @arrgh @netwok.
    
                You can also call `cite`
                explicitly. #cite(<arrgh>, style: "ieee")
    
                #bibliography("works.bib")
            """.trimIndent()
        )

        checkCompiles(
            """
                This was already noted by
                pirates long ago. @arrgh
    
                Multiple sources say ...
                @arrgh @netwok.
    
                You can also call `cite`
                explicitly. #cite(<arrgh>, style: "american-chemical-society.csl")
    
                #bibliography("works.bib")
            """.trimIndent()
        )

        checkCompiles(
            """
                This was already noted by
                pirates long ago. @arrgh
    
                Multiple sources say ...
                @arrgh @netwok.
    
                You can also call `cite`
                explicitly. #cite(<arrgh>, style: path("american-chemical-society.csl"))
    
                #bibliography("works.bib")
            """.trimIndent()
        )

        checkCompiles(
            """
                This was already noted by
                pirates long ago. @arrgh
    
                Multiple sources say ...
                @arrgh @netwok.
    
                You can also call `cite`
                explicitly. #cite(<arrgh>, style: bytes(read("american-chemical-society.csl")))
    
                #bibliography("works.bib")
            """.trimIndent()
        )
    }

    "text.font" {
        checkCompiles(
            """
            #show math.equation: set text(
              font: (
                // Ignore that this regex actually misses some of the script glyphs...
                (name: "XITS Math", covers: regex("[\u{1D49C}-\u{1D503}]")),
                "New Computer Modern Math"
              ),
              stylistic-set: 1,
            )
            $ cal(P)_i (X) * cal(C)_1 $
        """.trimIndent()
        )

        checkCompiles(
            """
                // reflect regex covers
                #set text(font: (name: "Ubuntu", covers: regex("\d")))
                #context assert.eq(text.font, (name: "ubuntu", covers: regex("\d")))
            """.trimIndent()
        )
    }

    "text.fill" {
        fun checkWith(@Language("typc") fillPattern: String) {
            checkCompiles(
                """
                    #set text(fill: $fillPattern)
                    Text
                """.trimIndent()
            )
            checkCompiles(
                """
                    #text(fill: $fillPattern)[Text]
                """.trimIndent()
            )
            checkCompiles(
                """
                    #text($fillPattern)[Text]
                """.trimIndent()
            )
            checkCompiles(
                """
                    #text(fill: $fillPattern, "Text")
                """.trimIndent()
            )
            checkCompiles(
                """
                    #text("Text", $fillPattern)
                """.trimIndent()
            )
            checkCompiles(
                """
                    #text("Text", $fillPattern)
                """.trimIndent()
            )
            checkCompiles(
                """
                    #set text($fillPattern)
                    Text
                """.trimIndent()
            )
        }

        checkWith("red")
        checkWith("oklab(27%, 20%, -3%, 50%)")
        checkWith("gradient.linear(..color.map.rainbow)")
        checkWith(
            """
            tiling(size: (30pt, 30pt))[
              #place(line(start: (0%, 0%), end: (100%, 100%)))
              #place(line(start: (0%, 100%), end: (100%, 0%)))
            ]
        """.trimIndent()
        )
    }

    "cetz import" {
        mapOf(
            "/main.typ" to """

            """.trimIndent()
        ) + additionalFiles
        val ctx = runtime.fileContext {
            singleFile(
                "/main.typ", """
                            #{
                            import "@preview/fletcher:0.5.8" as fletcher: diagram as dia, node, edge

                            [
                                = Total Store Order
                            ]

                            let diagram(..args) = align(center)[
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

                            diagram(
                                node((0, 0), [#h(2em) CPU 1 #h(2em)], name: <cpu1>),
                                node((.8, 0), [#h(2em) CPU 2 #h(2em)], name: <cpu2>),
                                node((0.15, 1), [buffer], name: <buf1>),
                                node((0.65, 1), [buffer], name: <buf2>),
                                node((0.4, 2), [#h(6em) Memory #h(6em)], name: <mem>),

                                edge(<cpu1>, "dd", "<{-", shift: -.5, label-side: center, [read]),
                                edge(<cpu2>, "dd", "<{-", shift: 0.2, label-side: center, [read]),

                                edge(<buf1>, "u", "<{-", label-side: center, [write]),
                                edge(<buf2>, "u", "<{-", label-side: center, [write]),
                                edge(<buf1>, "d", "-}>"),
                                edge(<buf2>, "d", "-}>", label-side: right, [write back]),
                            )
                            }
                        """.trimIndent().toByteArray()
            )

            inPackage(null) { it, _ -> RResult.Err(FileError.NotFound(it)) }

            fallback { it, session -> runtime.resolvePackage(it) }
        }
        try {
            runtime.compilePng(ctx)
        } finally {
            ctx.release()
        }
    }

})