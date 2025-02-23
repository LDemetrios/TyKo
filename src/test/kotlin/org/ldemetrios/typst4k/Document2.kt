//package org.ldemetrios.tyko
//
//import org.ldemetrios.tyko.compiler.Typst
//import org.ldemetrios.tyko.compiler.TypstDefaultCompiler
//import org.ldemetrios.tyko.compiler.compile
//import org.ldemetrios.tyko.model.*
//import org.ldemetrios.tyko.model.repr
//import org.ldemetrios.tyko.operations.*
//import org.ldemetrios.tyko.operations.std.evalMarkup
//import org.ldemetrios.tyko.operations.std.lorem
//import java.nio.file.Path
//
//fun main() {
//    TypstDefaultCompiler.compiler =
//        Typst(executable)
//
//    val content = TSequence {
//        +TSetPage(height = TAuto)
//        +THeading(depth = 1.t, body = TSequence { +"Methods" })
//        +TSpace()
//        +"We follow the glacier melting models established earlier."
//        +TParbreak()
//        +lorem(15)
//        +TParbreak()
//        +"Total displaced soil by glacial flow:"
//        +TParbreak()
//        +evalMarkup(
//            "\$ 7.32 beta + sum_(i=0)^nabla (Q_i (a_i - epsilon)) / 2 \$"
//        )
//    }
//    Typst().compile("#" + content.repr(), Path.of("example.png"))
//}
