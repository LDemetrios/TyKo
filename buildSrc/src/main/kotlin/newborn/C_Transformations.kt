package newborn

fun collectRawUnions(union: UnionType): List<List<String>> {
    val res = mutableListOf<List<String>>()
    val unions = mutableListOf(union)
    while (unions.isNotEmpty()) {
        val next = unions.removeLast()
        res.add(next.options.map { it.ident })
        for (option in next.options) {
            val params = option.params
            if (!params.isNullOrEmpty()) {
                unions.addAll(params)
            }
        }
    }
    return res
}
fun kebabToLowerCamel(what: String): String = kebabToTitleCamel(what)
    .replaceFirstChar { it.lowercase() }
    .let{if (it == "size") "sz" else it}

fun UnionType.toInternalDeclaration(): String = if (this.options.size == 1) options[0].toInternalDeclaration()
else {
    options.joinToString(", ", "UnionType(", ")") { it.toInternalDeclaration() }
}

fun Type.toInternalDeclaration(): String = when {
    ident == "*" || ident == "any" -> "AnyType"
    ident.first().isUpperCase() -> ident
    else -> {
        params
            ?.joinToString(", ", "ConcreteType(\"${this.ident}\", listOf(", "))") {
                it.toInternalDeclaration()
            }
            ?: "ConcreteType(\"${this.ident}\")"
    }
}
