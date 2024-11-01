package newborn

import generator.isSubsetOf
import generator.kebabToTitleCamel
import org.ldemetrios.utilities.castOrNull
import java.io.File

fun <T> Iterable<T>.jts(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = Int.MAX_VALUE,
    truncated: CharSequence = "...",
    transform: ((T) -> CharSequence)? = Any?::toString
): String = joinToString(separator, prefix, postfix, limit, truncated, transform)

data class DeclarationsMetadata(
    val allUnions: Set<Set<String>>,
    val allGenerics: Map<String, List<TypeParam>>,
    val kinds: Map<String, String>,
    val declarations: List<Declaration>,
//    val serializer: MutableMap<String, String> = mutableMapOf(),
)

const val supertype = "TValue"

val Declaration.kind
    get() = when (this) {
        is EnumDeclaration -> "enum"
        is PrimitiveDeclaration -> "primitive"
        is TypeDeclaration -> kind
    }

fun File.writeByAnyMeans(text: StringBuilder.() -> Unit) {
    parentFile.mkdirs()
    if (exists()) delete()
    createNewFile()
    writeText(
        StringBuilder().apply { text() }.toString()
    )
}

fun generateFor(declarations: List<Declaration>) {
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
                .map(ClassElem::type)
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
            if (declaration.name in listOf("str", "type", "function", "dictionary", "content")) continue

            val parent = declaration.castOrNull<TypeDeclaration>()?.parent?.let { "/$it" } ?: ""
            val location = "src/main/kotlin/org/ldemetrios/typst4k/model/auto$parent/" +
                    kebabToUpperCamel(declaration.name) + ".kt"
            File(location).writeByAnyMeans {
                writeDeclaration(declaration)
            }
        }

        writeSemiauto()
    }
}

context(DeclarationsMetadata)
fun StringBuilder.writeDeclaration(declaration: Declaration) {
    prelude()
    when (declaration) {
        is PrimitiveDeclaration -> writePrimitive(declaration)
        is EnumDeclaration -> writeEnum(declaration)
        is TypeDeclaration -> when (declaration.kind) {
            "class" -> writeClass(declaration)
            "type" -> writeType(declaration)
            "element" -> writeElement(declaration)
            "synthetic" -> writeSynthetic(declaration)
            else -> throw AssertionError(declaration.kind)
        }
    }
}

context(DeclarationsMetadata)
fun StringBuilder.writePrimitive(decl: PrimitiveDeclaration) {
    val maybeDelegate = if (decl.delegate) {
        "${decl.impl} by value,"
    } else ""


    "data class T${decl.name.upCam}${decl.params.bounded}(val value: ${decl.impl})".ln()
    " : $maybeDelegate $supertype${decl.unionSupers} {".ln()

    """
        |    override fun type() : TType = T${decl.name.upCam}
        |    override fun format() = Representations.reprOf(value)
        |    
        |    companion object : TType("${decl.name}") {
        |    }
        |}
        |
    """.cl()
}

context(DeclarationsMetadata)
fun StringBuilder.writeEnum(decl: EnumDeclaration) {
    "enum class T${decl.name.upCam}(override val value: String) : TStr${decl.unionSupers} {".ln()

    for (option in decl.variants) {
        """    ${(option).upCam}("$option"),""".ln()
    }
    """
        |    ;
        |    companion object {
        |        fun of(str: TStr) = of(str.value)
        |        
        |        fun of(str: String) : T${decl.name.upCam}? = when(str) {
        |        
    """.cl()
    for (option in decl.variants) {
        """            "$option" -> T${decl.name.upCam}.${option.upCam}""".ln()
    }
    """
        |            else -> null
        |        }
        |    }
        |}
        |
        |
    """.cl.ln()
}

context(DeclarationsMetadata)
fun StringBuilder.writeClass(decl: TypeDeclaration) {
    "data ${decl.fields.classType} T${decl.name.upCam}".ln()

    if (decl.fields.isNotEmpty()) {
        "(".ln()
        writeFields(decl)
        ")"()
    }
    decl.parent?.let {
        " : T${it.ident.upCam}"()
    }
    "{".ln()
    if (decl.sprepr) {
        "    override fun format() = Representations.reprOf(this)".ln()
    } else {
        "    override fun format() = ${decl.structRepr}".ln()
    }
    "".ln()
    if (decl.fields.isNotEmpty()) companion(decl, null)
    "}".ln()

}

context(DeclarationsMetadata)
fun StringBuilder.writeType(declaration: TypeDeclaration) {
    if (declaration.abstract) {
        append("interface T")
        append(kebabToUpperCamel(declaration.name))
        declaration.params.bounded()
        if (declaration.parent == null) {
            append(" : TValue")
        } else {
            append(" : T")
            append(kebabToUpperCamel(declaration.parent.ident))
            append("()")
        }
        unionSupertypes(setOf(declaration.name), listOf())
        append(" {\n")
        writeFields(declaration, param = false)

        append("    override fun format() = ")
        if (declaration.sprepr) {
            append("Representations.reprOf(this)\n")
        } else {
            append("Representations.structRepr(\n")
            writeReprEntries(declaration)
            append("\t)\n")
        }
        append("\toverride fun type(): TType = T")
        append(kebabToUpperCamel(declaration.name))
        append("\n")
        companion(declaration, "type")
        append("}\n")
    } else {
        append("data ")
        if (declaration.fields.isEmpty()) append("object")
        else append("class")
        append(" T")
        append(kebabToUpperCamel(declaration.name))
        if (declaration.fields.isNotEmpty()) {
            declaration.params.bounded()
            append("(\n")
            writeFields(declaration)
            append(")")
        }
        if (declaration.parent == null) {
            append(" : TValue")
        } else {
            append(" : T")
            append(kebabToUpperCamel(declaration.parent.ident))
//            append("()")
        }
        unionSupertypes(setOf(declaration.name), listOf())
        append(" {\n")
        append("    override fun format() = ")
        if (declaration.sprepr) {
            append("Representations.reprOf(this)\n")
        } else {
            append("Representations.structRepr(\n")
            writeReprEntries(declaration)
            append("\t)\n")
        }
        append("\toverride fun type(): TType = T")
        append(kebabToUpperCamel(declaration.name))
        if (declaration.fields.isNotEmpty()) {
            append("\n")
            companion(declaration, "type")
        } else {
            append("Type\n")
        }
        append("}\n")
        if (declaration.fields.isEmpty()) {
            append("data object T")
            append(kebabToUpperCamel(declaration.name))
            append("Type : TType(\"")
            append(declaration.name)
            append("\")\n")
        }
    }
}


context(DeclarationsMetadata)
fun StringBuilder.writeElement(decl: TypeDeclaration) {
    append("data class T")
    append(kebabToUpperCamel(decl.name))
    decl.params.bounded()
    append("(\n")
    writeFields(decl)
    append("\toverride val label:TLabel? = null,\n")
    append(") : TContent()")
    unionSupertypes(setOf(decl.name), listOf())
    append(" {\n")
    append("    override fun format() = ")
    if (decl.sprepr) {
        append("Representations.reprOf(this)\n")
    } else {
        append("Representations.elementRepr(\n")
        writeReprEntries(decl)
        append("\t)\n")
    }
    append("\toverride fun func() = T")
    append(kebabToUpperCamel(decl.name))
    append("\n")
    companion(decl, "element")
    append("}\n\n")

    if (decl.fields.any{it.settable}) {
        "data class TSet${decl.name.upCam}(".ln()
        writeFields(decl, settableOnly = true)
        ") : TSetRule(\"${decl.name}\") {".ln()
        append("    override fun format() = ")
        if (decl.spreprset) {
            append("Representations.reprOf(this)\n")
        } else {
            append("Representations.setRepr(\n")
            writeReprEntries(decl, settableOnly = true)
            append("\t)\n")
        }
        append("}\n\n")
    }
}

context(DeclarationsMetadata)
fun StringBuilder.writeSynthetic(declaration: TypeDeclaration) {
    val dictionarySuper = UnionType(declaration.fields.flatMap { it.type.options }.distinct())
    append("data class T")
    append(kebabToUpperCamel(declaration.name))
    declaration.params.bounded()
    append("(\n")
    writeFields(declaration)
    append(") : TDictionary<")
    concreteType(dictionarySuper)
    append(">()")
    unionSupertypes(setOf(declaration.name), listOf())
    append(" {\n")
    append("\toverride val value: Map<String, ")
    concreteType(dictionarySuper)
    append("> get() = mapOf(\n")
    for (field in declaration.fields) {
        append("\t\t\t\"${field.name}\" to ")
        append(kebabToLowerCamel(field.name))
        append(",\n")
    }
    append("\t)")
    if(declaration.fields.any{!it.required}) append( ".filterValues { it != null }.castUnchecked()" )
    append("\n")


    companion(declaration, null)
    append("}\n\n")
}

// SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS

context(DeclarationsMetadata)
fun StringBuilder.fieldType(field: ClassElem, skipNullability: Boolean = false) {
    if (field.variadic) append("TArray<")
    concreteType(field.type, substitute = true)
    if (field.variadic) append(">")
    if (!skipNullability && !field.required) append("?")
}


private val ClassElem.actualType get() = if (variadic) UnionType(listOf(Type("array", listOf(type)))) else type

context(DeclarationsMetadata)
fun StringBuilder.companion(decl: TypeDeclaration, kind: String?) {

    val generic = decl.params.isNotEmpty()

    val maybeSuper = if (kind != null) ": T${kind.upCam}(\"${decl.name}\")" else ""
    "    companion object $maybeSuper {".ln()
    for (field in decl.fields) {
        if (!generic || decl.params.all { field.type.hasNoReferencesOf(it) }) {
            append("        internal val ${field.name.lwCam}Type : InternalType = ")
            append(field.actualType.toInternalDeclaration())
            append("\n")
        } else {
            append("        internal fun ${field.name.lwCam}Type(${decl.params.jts(", ") { it.name + ": InternalType" }}) : InternalType = ")
            append(field.actualType.toInternalDeclaration())
            append("\n")
        }
    }
    append("\n")
    "    }".ln()
}


context(DeclarationsMetadata)
fun StringBuilder.writeFields(typeDeclaration: TypeDeclaration, param: Boolean = true, settableOnly: Boolean = false) {
    val fields = typeDeclaration.fields.positionalFirst()
    for (field in fields) if(!settableOnly || field.settable){
        append("\t")
        if (!param) append("abstract ")
        append("val ")
        append(kebabToLowerCamel(field.name))
        append(": ")
        fieldType(field)
        if (!field.required) {
            if (param) append(" = null")
        }
        if (param) append(",")
        append("\n")
    }
}

context(DeclarationsMetadata)
fun StringBuilder.writeReprEntries(type: TypeDeclaration, settableOnly: Boolean = false) {
    append("\t\t\"")
    append(type.name)
    append("\",\n")
    for (elem in type.fields) if (!settableOnly || elem.settable){
        append("\t\tArgumentEntry(")
        append(elem.variadic)
        append(", ")
        if (elem.positional) append("null")
        else append("\"" + elem.name + "\"")
        append(", ")
        append(kebabToLowerCamel(elem.name))
        append("),\n")
    }
}

context(DeclarationsMetadata)
fun StringBuilder.concreteType(type: UnionType, substitute: Boolean = false) {
    if (type.options.singleOrNull()?.ident == "*") {
        if (substitute) append("TValue") else append("*")
        return
    }
    val capital = type.options.map { it.ident.first().isUpperCase() }.distinct().single()
    if (capital) {
        append(type.options.single().ident)
        return
    }
    append("T")
    append(type.options.jts("Or") { kebabToUpperCamel(it.ident) })
    val generics = type.options.mapNotNull { it.params }.flatten()
    if (generics.isNotEmpty()) {
        append("<")
        for (g in generics) {
            concreteType(g)
            append(", ")
        }
        append(">")
    }
}

context(DeclarationsMetadata)
private fun StringBuilder.deserializingHeader(name: String, params: List<TypeParam>) {
    append("internal fun deserialize")
    append(kebabToUpperCamel(name))
    append("(actualType: InternalType, json: JSStuff, pathToValue: String) : T")
    append(kebabToUpperCamel(name))
    if (params.isNotEmpty()) {
        append("<")
        append(params.jts(", ") { "*" })
        append(">")
    }
    append(" {\n")
}

context(DeclarationsMetadata)
fun StringBuilder.unionSupertypes(
    union: Set<String>,
    generics: List<TypeParam>,
) {
    allUnions
        .filter { union.isSubsetOf(it) && union != it }
        .forEach { superset ->
            append(", \n    ")
            append("T")
            append(superset.sorted().jts("Or", transform = ::kebabToTitleCamel))
            val superGenerics = genericParametersOfUnion(superset)
            if (superGenerics.isNotEmpty()) {
                append("<")
                append(superGenerics.jts(", ") {
                    when {
                        it in generics -> it.name
                        it.variance.trim() == "out" -> "Nothing"
                        it.variance.trim() == "in" -> supertype
                        else -> throw AssertionError()
                    }
                })
                append(">")
            }
        }
}

context(DeclarationsMetadata)
fun genericParametersOfUnion(union: Set<String>): MutableList<TypeParam> {
    val genericParametersOfUnion = mutableListOf<TypeParam>()
    for (variant in union) {
        val generic = allGenerics[variant] ?: continue
        genericParametersOfUnion.addAll(generic)
    }
    return genericParametersOfUnion
}
