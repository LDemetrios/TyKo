//package org.ldemetrios.tyko
//
//import org.ldemetrios.tyko.compiler.*
//import org.ldemetrios.tyko.model.*
//import org.ldemetrios.tyko.model.repr
//import org.ldemetrios.tyko.operations.*
//import java.nio.file.Path
//
//fun main() {
//    println(
//        TPattern(
//            sz = TArray(30.pt, 30.pt),
//            body = TSequence {
//                        +TPlace(body = TLine(start = TArray(0.pc, 0.pc), end = TArray(100.pc, 100.pc)))
//                +TPlace(body = TLine(start = TArray(0.pc, 100.pc), end = TArray(100.pc, 0.pc)))
//            }
//        ).repr()
//    )
//
////    val typst = Typst("/home/user/.cargo/bin/typst")
////    typst.compile(Path.of("test2.typ")) {
////        format = OutputFormat.SVG
////        ppi = 1440
////    }
//
//    Typst("typst", "./typst-custom-serial") {
//        root = Path.of("src/typ")
//        ppi = 1440
//        compile {
//            input("mode", "heavy")
//        }
//        query {
//            input("mode", "lite")
//        }
//        watch {
//            ppi = 144
//        }
//    }
//
////    val typst = Typst("./typst") {
////        var inputs: MutableMap<String, String> = mutableMapOf(),
////        var fontPath: Path? = null,
////        var ppi: Int? = null,
////        var format: OutputFormat = OutputFormat.PDF,
////        val compileConfig : CompileConfig = CompileConfig(),
////        val queryConfig : QueryConfig = QueryConfig(),
////        val watchConfig : WatchConfig = WatchConfig(),
////    }
//
////    val x = typst.query<TValue>(
////        Path.of("src/test/resources/org/ldemetrios/tyko/test2.typ"),
////        "<full>",
////    ).orElseThrow().value.single()
////    println(x.repr())
//}
