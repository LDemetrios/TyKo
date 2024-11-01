package newborn

import com.github.h0tk3y.betterParse.grammar.tryParseToEnd
import com.github.h0tk3y.betterParse.parser.parse
import com.github.h0tk3y.betterParse.parser.parseToEnd
import com.github.h0tk3y.betterParse.parser.toParsedOrThrow
import org.ldemetrios.parsers.Tracer
import java.io.File
import kotlin.reflect.jvm.javaType
import kotlin.reflect.typeOf

fun main() {
    val files = listOf(
        "__automated_enums",
        "automated-updated",
        "others",
    ).map {
        println(it)
        File("buildSrc/src/main/resources/types/$it").readText()
    }.flatMap {
        modelParser.tryParseToEnd(it).toParsedOrThrow().value
    }
    File("src/main/kotlin/org/ldemetrios/typst4k/model/auto").deleteRecursively()
    generateFor(files)

//   val x =  """out"""
//
//   println( modelParser.tryParseToEnd(x).toParsedOrThrow().value)
}