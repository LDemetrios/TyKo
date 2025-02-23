package newborn

import com.github.h0tk3y.betterParse.grammar.tryParseToEnd
import com.github.h0tk3y.betterParse.parser.toParsedOrThrow
import java.io.File

import org.ldemetrios.kotlinpoet.FileSpecBuilder


fun main(vararg args: String) {
    val root = args.getOrElse (0) { "/home/ldemetrios/Workspace/TypstNKotlinInterop/TyKo" }

    val declarations = listOf(
        "__automated_enums",
        "automated-updated",
        "others",
    ).asSequence().map {
        println(it)
        File("$root/buildSrc/src/main/resources/types/$it").readText()
    }.flatMap {
        modelParser.tryParseToEnd(it).toParsedOrThrow().value
    }.toList()
    File("$root/src/main/kotlin/org/ldemetrios/tyko/model/auto").deleteRecursively()

    val fieldsUnions = declarations
        .asSequence()
        .filterIsInstance<TypeDeclaration>()
        .flatMap { it.fields }
        .map { it.type }
        .flatMap { collectRawUnions(it) }

    val syntheticUnions = declarations
        .filterIsInstance<TypeDeclaration>()
        .filter { it.kind == "synthetic" }
        .map {
            it.fields
                .map(Field::type)
                .flatMap(UnionType::options)
                .map(Type::ident)
        }

    val allUnions = (fieldsUnions + syntheticUnions)
        .distinct()
        .map { it.toSet() }
        .filter { it.size > 1 }
        .toSet()

    val generics = declarations
        .filterIsInstance<GenericDeclaration>()
        .filter { it.params.isNotEmpty() }
        .associate { it.name to it.params }

    val kinds = declarations.associate { it.name to it.kind }

    val metadata = DeclarationsMetadata(allUnions, generics, kinds, declarations)

    with(metadata) {
        for (declaration in declarations) {
            if (declaration.name in listOf("type", "function", "content")) continue

            val parent = declaration.castOrNull<TypeDeclaration>()?.parent?.let { "/$it" } ?: ""

            if (declaration is EnumDeclaration && !declaration.literal) continue
            modelFile(declaration.name.upCam) {
                writeDeclaration(declaration)
            }.writeToFile(File("$root/src/main/kotlin/org/ldemetrios/tyko/model/auto$parent/"))
//            File(location).writeByAnyMeans {
//                writeDeclaration(declaration)
//            }
        }

        writeSemiauto(root)
    }
}

context(DeclarationsMetadata)
fun FileSpecBuilder.writeDeclaration(declaration: Declaration): Boolean {
    when (declaration) {
        is PrimitiveDeclaration -> writePrimitive(declaration)
        is EnumDeclaration -> {
            if (declaration.literal) writeLiteralEnum(declaration)
            else return false
        }
        is TypeDeclaration -> when (declaration.kind) {
//            "class" -> writeClass(declaration)
//            "type" -> writeType(declaration)
//            "element" -> writeElement(declaration)
//            "synthetic" -> writeSynthetic(declaration)
            else -> writeType(declaration)
        }
    }

    return true
}
