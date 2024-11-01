package newborn

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
}

class UnionType(options: List<Type>) {
    val options: List<Type> = options.sortedBy { it.ident }
    override fun toString(): String = options.joinToString("|")
    fun hasNoReferencesOf(p: TypeParam): Boolean = options.all { it.hasNoReferencesOf(p) }
}

data class ClassElem(val modifiers: List<String>, val name: String, val type: UnionType) {
    val variadic: Boolean get() = "var" in modifiers
    val settable: Boolean get() = "set" in modifiers
    val positional: Boolean get() = "pos" in modifiers
    val required: Boolean get() = "req" in modifiers
}

sealed interface Declaration {
    val modifiers: List<String>
    val name: String

    val sprepr: Boolean get() = "sprepr" in modifiers
    val spreprset: Boolean get() = "spreprset" in modifiers
    val abstract: Boolean get() = "abstract" in modifiers
    val spser: Boolean get() = "spser" in modifiers
    val hidden: Boolean get() = "hidden" in modifiers
}

data class EnumDeclaration(
    override val modifiers: List<String>,
    override val name: String,
    val variants: List<String>
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
    val fields: List<ClassElem>,
    val parent: Type?
) : GenericDeclaration

data class TypeParam(val variance: String, val name: String) {
    override fun toString(): String = "$variance$name"
}
