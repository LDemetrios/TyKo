package org.ldemetrios.tyko.tests

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.ldemetrios.tyko.compiler.IndexedMark
import org.ldemetrios.tyko.compiler.SyntaxKind
import org.ldemetrios.tyko.compiler.SyntaxKind.*
import org.ldemetrios.tyko.compiler.SyntaxMark
import org.ldemetrios.tyko.compiler.SyntaxMode
import org.ldemetrios.tyko.driver.chicory.ChicoryTypstCore
import org.ldemetrios.tyko.driver.chicory.defaultWasiOptions
import org.ldemetrios.tyko.model.TArgs
import org.ldemetrios.tyko.model.TArray
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.THeading
import org.ldemetrios.tyko.model.TWhereSelector
import org.ldemetrios.tyko.model.repr
import org.ldemetrios.tyko.model.t
import org.ldemetrios.tyko.runtime.TypstRuntime
import org.ldemetrios.tyko.runtime.withInputsOrThrow

class Various : FreeSpec({
    System.setProperty("kotest.assertions.collection.print.size", "1000")

    val runtime = TypstRuntime(
        ChicoryTypstCore(
            defaultWasiOptions()
        )
    )

    "Formatting source" {
        (
                runtime.formatSource("""#for i in range(- 5){[a]}""", 120, 4)
                ) shouldBe "#for i in range(-5) { [a] }\n"
    }

    "Parsing source" {
        runtime
            .parseSyntax("""#for i in range(5) [a]""", mode = SyntaxMode.Markup)
            .marks shouldBe marks {
            Markup {
                Hash("#")
                ForLoop {
                    For("for")
                    Space(" ")
                    Ident("i")
                    Space(" ")
                    In("in")
                    Space(" ")
                    FuncCall {
                        Ident("range")
                        Args {
                            LeftParen("(")
                            Int("5")
                            RightParen(")")
                        }
                    }
                    Space(" ")
                    ContentBlock {
                        LeftBracket("[")
                        Markup {
                            Text("a")
                        }
                        RightBracket("]")
                    }
                }
            }
        }
    }

    "Inputs work" {
        runtime.fontCollection().use { fonts ->
            runtime.library()
                .withInputsOrThrow(TDict(mapOf("a" to "b".t)), fonts, true)
                .use { library ->
                    val value = runtime.withSingleFile("#sys.inputs.a") {
                        runtime.evalMainRaw(this, stdlib = library, fonts = fonts)
                    }
                    println(value.output)
                }
        }
    }

    "Queries resolve" {
        runtime.fontCollection().use { fonts ->
            runtime.withSingleFile(
                """
                    #set page(numbering: "1")

                    #heading(outlined: false)[
                      Table of Contents
                    ]
                    #context {
                      let chapters = query(
                        heading.where(
                          level: 1,
                          outlined: true,
                        )
                      )
                      for chapter in chapters {
                        let loc = chapter.location()
                        let nr = numbering(
                          loc.page-numbering(),
                          ..counter(page).at(loc),
                        )
                        [#chapter.body #h(1fr) #nr \ ]
                      }
                    }

                    = Introduction
                    #lorem(10)
                    #pagebreak()

                    == Sub-Heading
                    #lorem(8)

                    = Discussion
                    #lorem(18)
                """.trimIndent()
            ) {
                val selector = TWhereSelector(
                    THeading.ELEM,
                    TArgs(TArray(listOf()), TDict(mapOf("level" to 1.t, "outlined" to true.t)))
                )
                val x: TArray<THeading> = runtime.query(this, selector)
                for (y in x) {
                    println(y.repr())
                }
            }
        }
    }
})

fun marks(body: MarksScope.() -> Unit) = MarksScope().apply { body() }.marks

class MarksScope {
    val marks: MutableList<IndexedMark> = mutableListOf()
    private var index = 0

    private fun mark(mark: SyntaxMark) {
        marks.add(IndexedMark(mark = mark, index = index))
    }

    operator fun SyntaxKind.invoke(text: String) {
        mark(SyntaxMark.NodeStart(kind = this))
        index += text.length
        mark(SyntaxMark.NodeEnd)
    }

    operator fun SyntaxKind.invoke(body: MarksScope.() -> Unit) {
        mark(SyntaxMark.NodeStart(kind = this))
        body()
        mark(SyntaxMark.NodeEnd)
    }
}
