package newborn

import org.ldemetrios.js.JSObject
import org.ldemetrios.utilities.castOrNull
import java.io.File

private val ClassElem.deser get() = if (required) "deserialize" else "maybeDeser"

context(DeclarationsMetadata)
fun writeSemiauto() {
    val special = "src/main/kotlin/org/ldemetrios/typst4k/model/semiauto"

    File("$special/Str.kt").writeByAnyMeans {
        prelude()
        append("interface TStr : TValue")
        unionSupertypes(setOf("str"), listOf())
        """
        |{
        |    override fun type() : TType = TStr
        |    abstract val value: String
        |    override fun format() = Representations.reprOf(this)
        |    companion object: TType("str") {
        |    }   
        |}
        """.cl.ln.invoke()
    }

    File("$special/Type.kt").writeByAnyMeans {
        prelude()
        "open class TType(val name: String) : TValue"()
        unionSupertypes(setOf("type"), listOf())
        """
        |{
        |    override fun type() : TType = TType
        |    override fun format() = Representations.reprOf(this)
        |    companion object: TType("type") {
        |    }   
        |}
        """.cl.ln()
    }

    File("$special/Function.kt").writeByAnyMeans {
        prelude()
        "interface TFunction : TValue"()
        unionSupertypes(setOf("function"), listOf())
        """
        |{
        |    override fun type() : TType = TFunction
        |    override fun format() = Representations.reprOf(this)
        |    companion object: TType("function") {
        |    }   
        |}
        |
        """.cl()
    }

    File("$special/Dictionary.kt").writeByAnyMeans {
        prelude()
        "abstract class TDictionary<out V: TValue> : Map<String, V>, TValue"()
        unionSupertypes(setOf("dictionary"), listOf())
        """
        |{
        |    override fun type() : TType = TDictionary
        |    override fun format() = Representations.reprOf(this)
        |    companion object: TType("dictionary") {
        |    }
        |    abstract val value : Map<String, V>
        |    override val entries: Set<Map.Entry<String, V>> get() = value.entries
        |    override val keys: Set<String> get() = value.keys
        |    override val size: Int get() = value.size
        |    override val values: Collection<V> get() = value.values
        |    override fun containsKey(key: String): Boolean = value.containsKey(key)
        |    override fun containsValue(value: @UnsafeVariance V): Boolean = this.value.containsValue(value)
        |    override fun get(key: String): V? = value.get(key)
        |    override fun isEmpty(): Boolean = value.isEmpty()
        |    override fun forEach(action: java.util.function.BiConsumer<in String, in V>) = value.forEach(action)
        |    override fun getOrDefault(key: String, defaultValue: @UnsafeVariance V): V = value.getOrDefault(key, defaultValue)
        |}
        """.cl.ln()
    }

    File("$special/Content.kt").writeByAnyMeans {
        prelude()
        "abstract class TContent : TValue"()
        unionSupertypes(setOf("content"), listOf())
        """
            |{
            |    abstract val label: TLabel?
            |    abstract fun func(): TElement
            |    override fun type(): TType = TContent
            |    companion object : TType("content") {
            |    }
            |}
        """.cl.ln()
    }


    File("$special/Unions.kt").writeByAnyMeans {
        prelude()
        for (union in allUnions) {
            "@UnionSyntheticType(options = [${union.jts(", ") { "\"$it\"" }}])".ln()
            "sealed interface T${union.jts("Or") { it.upCam }}"()
            union.mapNotNull { allGenerics[it] }.flatten().bounded()
            " : TValue"()
            unionSupertypes(union, union.flatMap { allGenerics[it] ?: listOf() })
            ";\n\n"()
        }
    }

    File("$special/SemiautoDeserialization.kt").writeByAnyMeans {
        prelude()
        val S = "$"
        val atPath = "at path \\\"\$path\\\""

        val enums = declarations.filterIsInstance<EnumDeclaration>()
        """
       |private infix fun String.w(value: (String) -> TStr?) = this to value
       |
       |internal val enumTypes = mutableMapOf<String, (String) -> TStr?>(
        """.cl.ln()
        for (enum in enums) {
            "    \"${enum.name}\".w(T${enum.name.upCam}::of),".ln()
        }
        ")".ln.ln()

        "internal val deserializers = mutableMapOf<String, (JSStuff, InternalType,  String) -> TValue>(".ln()
        "\t\"element\" to ::deserializeElement,".ln()
        for (decl in declarations) if (decl is TypeDeclaration && !decl.abstract) {
            "    \"${decl.name}\" to ::deserialize${decl.name.upCam},".ln()
        }
        for (decl in declarations) if (decl is TypeDeclaration && decl.kind == "element" && decl.fields.any { it.settable }) {
            "    \"set-${decl.name}\" to ::deserializeSet${decl.name.upCam},".ln()
        }
        ")".ln.ln()

        "internal val syntheticTypes = mutableMapOf<String, (JSObject, InternalType, String) -> TValue>(".ln()
        for (decl in declarations) if (decl is TypeDeclaration && decl.kind == "synthetic") {
            "    \"${decl.name}\" to ::deserialize${decl.name.upCam},".ln()
        }
        ")".ln.ln()

        "internal val genericTypes = mutableMapOf<String, Int>(".ln()
        for (decl in declarations) if (decl is GenericDeclaration) {
            "    \"${decl.name}\" to ${decl.params.size},".ln()
        }
        ")".ln.ln()


        for (decl in declarations) if (decl is TypeDeclaration && !decl.abstract && !decl.spser) {
            typeDeserializer(decl)
        }
    }
}

context(DeclarationsMetadata, StringBuilder)
fun typeDeserializer(decl: TypeDeclaration) {
    """
    |internal fun deserialize${decl.name.upCam}(value: JSStuff, type: InternalType, path: String) : TValue {
    |    val option = type.optionOf("${decl.name}")
    """.cl.ln()

    var parent = decl.parent?.ident
    while (parent != null) {
        """
    |        ?: type.optionOf("$parent")
        """.cl.ln()
        parent = declarations.find { it.name == parent }?.castOrNull<TypeDeclaration>()?.parent?.ident
    }

    "\tcheck(option != null) { \"`${decl.name}`, indicated by the type descriptor, is not present in `${'$'}type` options (at path ${'$'}path)\" }".ln()

    for ((ind, generic) in decl.params.withIndex()) {
        "\tval ${generic.name} = option.params?.getOrNull($ind) ?: AnyType".ln()
    }

    val genericArgs = if (decl.params.isEmpty()) "" else {
        decl.params.joinToString(", ", "(", ")") { it.name }
    }

    val typeParams = if (decl.params.isEmpty()) "" else {
        decl.params.joinToString(", ", "<", ">") { if (it.variance.trim() == "out") "TValue" else "Nothing" }
    }

    val anyFields = decl.fields.isNotEmpty() || decl.kind == "element"
    "\treturn T${decl.name.upCam}$typeParams ${if (anyFields) "(" else ""}".ln()

    for (field in decl.fields) {
        "\t\t${field.deser}(value[\"${field.name}\"], T${decl.name.upCam}.${field.name.lwCam}Type$genericArgs, \"\$path/${field.name}\").castUnchecked(),".ln()
    }
    if (anyFields) {
        "\t)".ln()
    }
    "}".ln.ln()

    /// Set rule
    if (decl.kind != "element" || decl.fields.none { it.settable }) return

    """
    |internal fun deserializeSet${decl.name.upCam}(value: JSStuff, type: InternalType, path: String) : TValue {
    |    val option = type.optionOf("${decl.name}") ?: type.optionOf("style")
    |           ?: failWith("`set ${decl.name}`, indicated by the type descriptor, is not present in `${'$'}type` options (at path ${'$'}path)" )
    |    val id = value["id"].cast<JSString>().str
    """.cl.ln()

    "\treturn TSet${decl.name.upCam}(".ln()
    for (field in decl.fields) if (field.settable) {
        "\t\t(if(id == \"${field.name}\")"()
        " ${field.deser}(value[\"value\"], T${decl.name.upCam}.${field.name.lwCam}Type$genericArgs, \"\$path/${field.name}\") else null).castUnchecked(),".ln()
    }
    "\t)".ln()
    "}".ln.ln()
}
