package newborn

import org.ldemetrios.kotlinpoet.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName

data class Type(val ident: String, val params: List<UnionType>?) {
    override fun toString(): String = StringBuilder().apply {
        append(ident)
        if (!params.isNullOrEmpty()) {
            append("<")
            append(params.joinToString(", "))
            append(">")
        }
    }.toString()

    fun hasNoReferencesOf(p: TypeParam): Boolean = (params?.all { it.hasNoReferencesOf(p) } ?: true) && ident != p.name

    fun toDelegate(): TypeName {
        if (ident.contains(".")) {
            // It's kotlin type
            val (simple, pack) = ident.reversed().split(".", limit = 2).map { it.reversed() }
            val classname = ClassName(pack, simple)
            return classname[(params?.map{it.toDelegate()} ?: listOf())]
        } else {
            return if (ident.first().isLowerCase()){
                ident.toClassname()
            } else {
                // It's supposedly a variable
                 TypeVariableName(ident)
            }
        }
    }
}

class UnionType(options: List<Type>) {
    val options: List<Type> = options.sortedBy { it.ident }
    override fun toString(): String = options.joinToString("|")
    fun hasNoReferencesOf(p: TypeParam): Boolean = options.all { it.hasNoReferencesOf(p) }
    fun toDelegate(): TypeName {
        when (options.size) {
            0 -> throw AssertionError()
            1 -> return options[0].toDelegate()
            else -> {
//                val opts = options.map{it.toDelegate()}
                // Assuming all are Typst classes, otherwise union is undefined
                throw AssertionError()
            }
        }
    }
}

data class Field(val modifiers: List<String>, val name: String, val type: UnionType) {
    val variadic: Boolean get() = "var" in modifiers
    val settable: Boolean get() = "set" in modifiers
    val positional: Boolean get() = "pos" in modifiers
    val required: Boolean get() = "req" in modifiers
}

@Suppress("unused")
sealed interface Declaration {
    val modifiers: List<String>
    val name: String

    val sprepr: Boolean get() = "sprepr" in modifiers
    val sum: Boolean get() = "sum" in modifiers
    val sealed: Boolean get() = "sealed" in modifiers
    val literal: Boolean get() = "literal" in modifiers
    val spreprset: Boolean get() = "spreprset" in modifiers
    val abstract: Boolean get() = "abstract" in modifiers
    val spser: Boolean get() = "spser" in modifiers
    val hidden: Boolean get() = "hidden" in modifiers
}

data class EnumDeclaration(
    override val modifiers: List<String>,
    override val name: String,
    val variants: List<String>,
    val parent: Type?,
) : Declaration

sealed interface GenericDeclaration : Declaration {
    val params: List<TypeParam>
}

data class PrimitiveDeclaration(
    override val modifiers: List<String>,
    override val name: String,
    override val params: List<TypeParam>,
    val delegate: Boolean,
    val impl: Type,
) : GenericDeclaration

data class TypeDeclaration(
    override val modifiers: List<String>,
    val kind: String,
    override val name: String,
    override val params: List<TypeParam>,
    val fields: List<Field>,
    val parent: Type?
) : GenericDeclaration

data class TypeParam(val variance: String, val name: String) {
    override fun toString(): String = "$variance$name"
    val poetVariance
        get() = when {
            variance.startsWith("out") -> KModifier.OUT
            variance.startsWith("in") -> KModifier.IN
            else -> throw AssertionError()
        }
}

val Declaration.kind
    get() = when (this) {
        is EnumDeclaration -> "enum"
        is PrimitiveDeclaration -> "primitive"
        is TypeDeclaration -> kind
    }