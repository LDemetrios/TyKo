//package org.ldemetrios.tyko
//
//import org.ldemetrios.tyko.compiler.Typst
//import org.ldemetrios.tyko.compiler.TypstDefaultCompiler
//import org.ldemetrios.tyko.compiler.compileAndRead
//import org.ldemetrios.tyko.model.*
//import org.ldemetrios.tyko.model.repr
//import org.ldemetrios.tyko.operations.*
//import org.ldemetrios.tyko.operations.std.lorem
//
//const val executable = "/home/ldemetrios/Workspace/_AnotherLayer/TypstNKotlin/typst-erased-serialize/target/release/typst"
//
//fun main() {
//    TypstDefaultCompiler.compiler =
//        Typst(executable)
//
//    println(1)
//    val content = TStyled(
//        TArray(TSetPage(height = TAuto)),
//        TSequence {
//            +THeading(depth = 1.t, body = TSequence { +"Methods" })
//            +TSpace()
//            +"We follow the glacier melting models established earlier."
//            +TParbreak()
//            +lorem(15)
//            +TParbreak()
//            +"Total displaced soil by glacial flow:"
//            +TParbreak()
//            +TMathEquation(block = true.t, body = TSequence {
//                +"7.32" + TSpace() + "β" + TSpace() + "+"
//                +TSpace()
//                +TMathAttach(
//                    "∑".text, t = "∇".text, b = TSequence { +"i" + "=" + "0" }
//                )
//                +TSpace()
//                +TSequence {
//                    +TMathAttach("Q".text, b = "i".text)
//                    +TSpace()
//                    +TMathLr(TSequence {
//                        +"("
//                        +TMathAttach("a".text, b = "i".text)
//                        +TSpace()
//                        +"−" + "ε" + ")"
//                    })
//                } // "2"
//            })
//        }
//    )
////    val contentMalformed = TStyled(
////        TSetPage(height = TAuto)
////    ) {
////        +THeading(depth = 1.t) { +"Methods" }
////        +" "
////        +"We follow the glacier melting models established earlier."
////        +TParbreak()
////        +lorem(15)
////        +TParbreak()
////        +"Total displaced soil by glacial flow:"
////        +TParbreak()
////        +TEquation(block = true.t) {
////            +"7.32" + " " + "β" + " " + "+"
////            +" "
////
////            +TAttach(
////                "∑".text, t = "∇".text, b = TSequence { +"i" + "=" + "0" }
////            )
////            +" "
////            +TSequence {
////                +TMathAttach("Q".text, b = "i".text)
////                +" "
////                +TMathLr {
////                    +"("
////                    +TMathAttach("a".text, b = "i".text)
////                    +" "
////                    +"−" + "ε" + ")"
////                }
////            } / "2"
////        }
////    }
//
//    println(content.repr())
////    println(contentMalformed.repr())
//
//    println(
//        Typst("/home/ldemetrios/Workspace/TypstNKotlinInterop/TyKo/typst012")
//            .compileAndRead("#" + content.repr())
//            .map { it.single() }
//            .map { Unit }
//    )
//}
//
//
////#{
////    heading(depth: 1, "Methods")
////    [ ]
////    "We follow the glacier melting models established earlier."
////    parbreak()
////    lorem(15)
////    parbreak()
////    "Total displaced soil by glacial flow:"
////    parbreak()
////    math.equation(
////        block: true,
////    {
////        "7.32"
////        [ ]
////        "β"
////        [ ]
////        "+"
////        [ ]
////        math.attach(
////            "∑",
////            t: "∇",
////        b: {
////        "i"
////        "="
////        "0"
////    },
////        )
////        [ ]
////        math.frac(
////            {
////                math.attach("Q", b: "i")
////                [ ]
////                math.lr({
////                    "("
////                    math.attach("a", b: "i")
////                    [ ]
////                    "−"
////                    [ ]
////                    "ε"
////                    ")"
////                })
////            },
////            "2",
////        )
////    },
////    )
////}
