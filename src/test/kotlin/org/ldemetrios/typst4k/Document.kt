package org.ldemetrios.typst4k

import org.ldemetrios.typst4k.dsl.Typst
import org.ldemetrios.typst4k.dsl.compile
import org.ldemetrios.typst4k.dsl.compileAndRead
import org.ldemetrios.typst4k.model.*
import org.ldemetrios.typst4k.rt.*
import java.io.File

object MathContextProvider

        context(MathContextProvider)
typealias TAttach = TMathAttach

        context(MathContextProvider)
typealias TFrac = TMathFrac

fun TEquation(
    block: TBool? = null,
    numbering: TFunctionOrNoneOrStr? = null,
    numberAlign: TAlignment? = null,
    supplement: TAutoOrContentOrFunctionOrNone? = null,
    label: TLabel? = null,
    body: MathContextProvider.() -> TContent,
) = TMathEquation(
    MathContextProvider.body(),
    block,
    numbering,
    numberAlign,
    supplement,
    label,
)

fun main() {
    val content = TSequence(THeading(body = "Methods".text, depth = 1.t),
        TSpace(),
        "We follow the glacier melting models established earlier.".text,
        TParbreak(),
        TCustomContent("lorem", listOf(15.t), mapOf()),
        TParbreak(),
        "Total displaced soil by glacial flow:".text,
        TParbreak(),
        TEquation(block = true.t) {
            TSequence(
                "7.32".text, TSpace(), "β".text, TSpace(), "+".text, TSpace(), TAttach(
                    "∑".text, t = "∇".text, b = TSequence("i".text, "=".text, "0".text)
                ), TSpace(), TFrac(
                    TSequence(
                        TMathAttach("Q".text, b = "i".text),
                        TSpace(),
                        TMathLr(
                            TSequence(
                                "(".text,
                                TMathAttach("a".text, b = "i".text),
                                TSpace(),
                                "−".text, TSpace(), "ε".text, ")".text,
                            )
                        ),
                    ), "2".text
                )
            )
        })
    println(content.repr())

    println(
        Typst("./typst")
            .compileAndRead("#set page(height:auto)\n #" + content.repr())
            .map { it.single() }
    )
}


//#{
//    heading(depth: 1, "Methods")
//    [ ]
//    "We follow the glacier melting models established earlier."
//    parbreak()
//    lorem(15)
//    parbreak()
//    "Total displaced soil by glacial flow:"
//    parbreak()
//    math.equation(
//        block: true,
//    {
//        "7.32"
//        [ ]
//        "β"
//        [ ]
//        "+"
//        [ ]
//        math.attach(
//            "∑",
//            t: "∇",
//        b: {
//        "i"
//        "="
//        "0"
//    },
//        )
//        [ ]
//        math.frac(
//            {
//                math.attach("Q", b: "i")
//                [ ]
//                math.lr({
//                    "("
//                    math.attach("a", b: "i")
//                    [ ]
//                    "−"
//                    [ ]
//                    "ε"
//                    ")"
//                })
//            },
//            "2",
//        )
//    },
//    )
//}
