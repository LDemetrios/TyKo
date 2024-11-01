package newborn

import generator.isSubsetOf
import generator.kebabToTitleCamel
import java.lang.StringBuilder

context(StringBuilder)
operator fun String.invoke() = append(this)

val String.cl get() = trimMargin()

fun build(func: StringBuilder.() -> Unit): String {
    val sb = StringBuilder()
    sb.func()
    return sb.toString()
}

val prelude = """
            |package org.ldemetrios.typst4k.model
            |
            |import org.ldemetrios.typst4k.rt.*
            |import org.ldemetrios.js.*
            |import kotlin.reflect.KType
            |import kotlin.reflect.typeOf
            |import org.ldemetrios.utilities.cast
            |import org.ldemetrios.utilities.castUnchecked
            |
            |
        """.trimMargin()

val String.ln get() = this + "\n"

val String.upCam get() = kebabToUpperCamel(this)
val String.lwCam get() = kebabToLowerCamel(this)

val List<TypeParam>.bounded
    get() = if (isEmpty()) "" else {
        joinToString(", ", "<", ">") { "$it : TValue" }
    }

context(DeclarationsMetadata)
val GenericDeclaration.unionSupers
    get() = unionSupers(
        setOf(this.name),
        this.params,
    )

context(DeclarationsMetadata)
val Declaration.unionSupers
    get() = unionSupers(
        setOf(this.name),
        listOf(),
    )

context(DeclarationsMetadata)
fun unionSupers(
    union: Set<String>,
    generics: List<TypeParam>,
) = allUnions
    .filter { union.isSubsetOf(it) && union != it }
    .map { superset ->
        val x = ", \n    " + ("T") + (superset.sorted().joinToString("Or", transform = ::kebabToTitleCamel))
        val superGenerics = genericParametersOfUnion(superset)
        val y = if (superGenerics.isNotEmpty()) {
            ("<") +
                    (superGenerics.joinToString(", ") {
                        when {
                            it in generics -> it.name
                            it.variance.trim() == "out" -> "Nothing"
                            it.variance.trim() == "in" -> supertype
                            else -> throw AssertionError()
                        }
                    }) +
                    (">")
        } else ""

        x + y
    }.joinToString("")


val List<ClassElem>.classType get() = if (isEmpty()) "object" else "class"

val ClassElem.named get() = if (positional) null else name

val TypeDeclaration.reprEntries get() = build {
    """        "$name",""".ln()
    for (elem in fields) {
        "        ArgumentEntry(${elem.variadic}, ${elem.named?.let{"\"$it\""}}, ${elem.name.lwCam}),".ln()
    }
}

val TypeDeclaration.structRepr get() = "Representations.structRepr(\n$reprEntries    )"