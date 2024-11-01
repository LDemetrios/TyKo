package org.ldemetrios.typst4k.model

internal sealed interface InternalType {
    fun optionOf(ident: String): ConcreteType?
}

internal data object AnyType : InternalType {
    override fun optionOf(ident: String): ConcreteType = ConcreteType(
        ident,
        List(genericTypes[ident] ?: 0) { AnyType }
    )

    override fun toString(): String = "any"
}

internal data class ConcreteType(val ident: String, val params: List<InternalType>? = null) : InternalType {
    override fun toString(): String = StringBuilder().apply {
        append(ident)
        if (!params.isNullOrEmpty()) {
            append("<")
            append(params.joinToString(", "))
            append(">")
        }
    }.toString()

    override fun optionOf(ident: String): ConcreteType? = if (ident == this.ident) this else null
}

internal fun UnionType(vararg options: ConcreteType) = UnionType(options.toList())

internal class UnionType(val options: List<ConcreteType>) : InternalType {
    override fun optionOf(ident: String): ConcreteType? = options.find { it.ident == ident }
    override fun toString(): String = options.joinToString("|")
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class UnionSyntheticType(val options: Array<String>)