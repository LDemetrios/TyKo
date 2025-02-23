package newborn

//import com.pinterest.ktlint.core.KtLint
//import com.pinterest.ktlint.ruleset.standard.StandardRuleSetProvider
import org.ldemetrios.kotlinpoet.*
import org.ldemetrios.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.*
import java.io.File
import java.lang.StringBuilder
import java.util.Locale

context(StringBuilder)
operator fun String.invoke() = append(this)

val String.cl get() = trimMargin()

inline fun <reified R> Any?.cast() = this as R
inline fun <reified R> Any?.castOrNull() = this as? R

fun FileSpec.writeToFile(file: File) = File("$file/${this.name}.kt").writeByAnyMeans(run {
    val sb = StringBuilder()
    this@writeToFile.writeTo(sb)
    val res = sb.toString()

    "@file:Suppress(\"unused\", \"RedundantVisibilityModifier\")" + "\n\n" +
            res.replace(Regex("(\r\n?|\n)(( {2})+)")) { it.groupValues[1] + it.groupValues[2].repeat(2) }
})

fun modelFile(name: String, setup: FileSpecBuilder.() -> Unit) = buildFileSpec(PACK, name) {
    addImport("kotlin.reflect", "KType", "typeOf")
    addImport("org.ldemetrios.utilities", "cast", "castUnchecked")
//    addImport("org.ldemetrios.tyko.operations", "*")
    addImport("org.ldemetrios.js", "JSObject", "JSString", "JSNumber", "JSBoolean", "JSUndefined")

    setup()
}

const val PACK = "org.ldemetrios.tyko.model"
val TVALUE = ClassName(PACK, "TValue")
val TDYNAMIC = ClassName(PACK, "TDynamic")
val TTYPE = ClassName(PACK, "TType")
val TTYPEIMPL = ClassName(PACK, "TTypeImpl")
val TELEMENT = ClassName(PACK, "TElement")
val TELEMENTIMPL = ClassName(PACK, "TElementImpl")
val TLABEL = ClassName(PACK, "TLabel")
val INTERNAL_TYPE = ClassName(PACK, "InternalType")
val TSTR = ClassName(PACK, "TStr")
val TARRAY = ClassName(PACK, "TArray")
val TCONTENT = ClassName(PACK, "TContent")
val TSETRULE = ClassName(PACK, "TSetRule")
val TDICTIONARY = ClassName(PACK, "TDictionary")
val TFUNCTION = ClassName(PACK, "TFunction")
val TNATIVEFUNCTION = ClassName(PACK, "TNativeFunction")

val TINTERFACETYPE = ClassName(PACK, "TInterfaceType")
val SERIALNAME = ClassName(PACK, "SerialName")
val TSETRULETYPE = ClassName(PACK, "TSetRuleType")
val TENUMTYPE = ClassName(PACK, "TEnumType")
val TSYNTHETICTYPE = ClassName(PACK, "TSyntheticType")
val TUNIONTYPE = ClassName(PACK, "TUnionType")
val TYPSTOVERLOADS = ClassName(PACK, "TypstOverloads")
val TCONTENTBODY = ClassName(PACK, "TContentBody")
val TVARARG = ClassName(PACK, "TVararg")

val String.ln get() = this + "\n"

val String.upCam get() = kebabToTitleCamel(this)
val String.lwCam get() = kebabToLowerCamel(this)

val List<TypeParam>.bounded get() = map { TypeVariableName(it.name, listOf(TVALUE), it.poetVariance) }

val Field.named get() = if (positional) null else name

context(DeclarationsMetadata)
val TypeDeclaration.allFields
    get() = generateSequence(this) {
        it.parentDecl()?.run { this as? TypeDeclaration ?: throw AssertionError("Non-type parent?!") }
    }
        .flatMap { it.fields }
        .toList()


context(DeclarationsMetadata)
fun FunSpecHandlerScope.structFormatFunction(
    decl: TypeDeclaration,
    function: String = "reprOf",
    settableOnly: Boolean = false,
    prependParams: CodeBlockBuilder.() -> Unit = { }
) {
    "format" {
        returns = STRING
        addModifiers(OVERRIDE)
        val block = buildCodeBlock {
            append("return Representations.$function(")
            append("%S,", decl.name)
            prependParams()
            for (field in decl.allFields) {
                if (settableOnly && !field.settable) continue
                append("ArgumentEntry(${field.variadic}, ${field.named?.let { "\"$it\"" }}, `%L`),", field.name.lwCam)
            }
            append(")")
        }
        append(block)
    }
}

fun FunSpecHandlerScope.specialFormatFunction() {
    "format" {
        returns = STRING
        addModifiers(OVERRIDE)
        appendLine("return Representations.reprOf(this)")
    }
}

fun FunSpecHandlerScope.exactlyFormatFunction(str: String) {
    "format" {
        returns = STRING
        addModifiers(OVERRIDE)
        appendLine("return %S", str)
    }
}

context(DeclarationsMetadata)
fun TypeSpecBuilder.addUnionSupertypes(name: String) = addUnionSupertypes(setOf(name), listOf())

context(DeclarationsMetadata)
fun TypeSpecBuilder.addUnionSupertypes(
    union: Set<String>,
    generics: List<TypeParam>,
) {
    allUnions
        .filter { union.isSubsetOf(it) && union != it }
        .forEach { superset ->
            val superGenerics = genericParametersOfUnion(superset).map {
                when {
                    it in generics -> ClassName(PACK, it.name)
                    it.variance.trim() == "out" -> TDYNAMIC
                    it.variance.trim() == "in" -> TVALUE
                    else -> throw AssertionError()
                }
            }

            this.addSuperinterface(superset.unionToClassname()[superGenerics])
        }
}

fun Set<String>.unionToClassname() = ClassName(
    PACK,
    this.sorted().jts("Or", prefix = "T", transform = ::kebabToTitleCamel)
)

fun Set<String>.sectToImplClassname() = ClassName(
    PACK,
    this.sorted().jts("And", prefix = "T", postfix = "Impl", transform = ::kebabToTitleCamel)
)

fun Set<String>.sectToClassname() = ClassName(
    PACK,
    this.sorted().jts("And", prefix = "T", transform = ::kebabToTitleCamel)
)

fun String.toClassname() = setOf(this).unionToClassname()
fun String.toImplClassname() = setOf(this).sectToImplClassname()

fun <T> Iterable<T>.jts(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = Int.MAX_VALUE,
    truncated: CharSequence = "...",
    transform: ((T) -> CharSequence)? = Any?::toString
): String = joinToString(separator, prefix, postfix, limit, truncated, transform)

fun File.writeByAnyMeans(text: String) {
    parentFile.mkdirs()
    if (exists()) delete()
    createNewFile()
    writeText(text)
}

fun File.writeByAnyMeans(text: StringBuilder.() -> Unit) {
    writeText(StringBuilder().apply(text).toString())
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

fun fieldType(
    field: Field,
    skipNullability: Boolean = false,
    ignoreVariadic: Boolean = false,
    removeGenerics: Boolean = false
): TypeName {
    var type = concreteType(field.type, substitute = true, removeGenerics = removeGenerics)

    if (field.variadic && !ignoreVariadic) type = TARRAY(type)
    if (!skipNullability && !field.required) type = type.nullable()
    return type
}

val Field.actualType get() = if (variadic) UnionType(listOf(Type("array", listOf(type)))) else type

fun concreteType(type: UnionType, substitute: Boolean = false, removeGenerics: Boolean = false): TypeName {
    return when {
        type.options.singleOrNull()?.ident == "*" -> {
            if (substitute) TVALUE else STAR
        }
        // capital
        type.options.map { it.ident.first().isUpperCase() }.distinct().single() -> {
            if (removeGenerics) TVALUE else TypeVariableName(type.options.single().ident)

        }

        else -> {
            val generics = type.options.mapNotNull { it.params }.flatten()

            type.options.map { it.ident }.toSet().unionToClassname()[
                generics.map {
                    concreteType(it, substitute = substitute, removeGenerics = removeGenerics)
                }
            ]
        }
    }
}

context(DeclarationsMetadata)
fun TypeSpecBuilder.writeDataClassFields(
    decl: TypeDeclaration,
    settableOnly: Boolean = false,
    onEach: PropertySpecBuilder.(Field) -> Unit = {},
    modifyPrimaryConstructor: FunSpecBuilder.() -> Unit = {}
) {
    setPrimaryConstructor {
        parameters {
            writeFields(decl, settableOnly = settableOnly, onEach = onEach, addAsVal = true)
        }
        this.modifyPrimaryConstructor()
    }
}

context(DeclarationsMetadata)
fun PropertySpecHandlerScope.writeFields(
    decl: TypeDeclaration,
    settableOnly: Boolean = false,
    reinit: Boolean = false
) {
    val fields = decl.fields.positionalFirst()
    for (field in fields) if (!settableOnly || field.settable) {
        (field.name.lwCam)(fieldType(field)) {
            if (reinit) {
                setInitializer(field.name.lwCam)
            }
        }
    }
}

context(DeclarationsMetadata)
fun ParameterSpecHandlerScope.writeFields(
    decl: TypeDeclaration,
    settableOnly: Boolean = false,
    addAsVal: Boolean = false,
    onEach: PropertySpecBuilder.(Field) -> Unit = {},
) {
    val fields = decl.allFields.positionalFirst()
    for (field in fields) if (!settableOnly || field.settable) {
        (field.name.lwCam)(fieldType(field)) {
            if (!field.required) setDefaultValue("null")
            if (addAsVal) addAsVal { onEach(field) }
        }
    }
}

fun kebabToTitleCamel(string: String): String {
    val qualifier = when (string.split(".").first()) {
        "list", "enum", "terms",
        "grid", "table", "state",
        "math", "counter", "footnote",
        "outline", "par", "raw",
                 "curve"
             -> string.replace(            ".",            "-"        )
        "gradient" -> string.removePrefix("gradient.") + "-gradient"
        else -> string
    }
    val words = qualifier.split(".").last().split("-")
    val result = StringBuilder()
    for (word in words) {
        result.append(word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
    }
    return result.toString()
}

fun <T> Set<T>.isSubsetOf(other: Set<T>) = this.all { it in other }

fun AnnotationSpecBuilder.addArrayMember(list: List<String>) =
    addMember(list.joinToString(", ", "[", "]") { "%S" }, *list.toTypedArray())