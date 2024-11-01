package newborn

import org.ldemetrios.parsers.opt

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

fun kebabToParts(what: String): List<String> {
    val path = what.split(".")
    val parts = when (path.first()) {
        /*"math",*/ "color" -> if (path.size == 1) path else path.drop(1)
        "gradient" -> path.drop(1) + "gradient"
        else -> path
    }.flatMap { it.split("-") }
    return parts.let {
        when (it) {
            listOf("class", "class") -> listOf("unicode", "math", "class")
            else -> it
        }
    }
}

fun partToUpperCamel(what: String): String = what[0].uppercase() + what.drop(1)

fun kebabToLowerCamel(what: String): String {
//    println(what)
    val parts = kebabToParts(what)
    val result = StringBuilder(parts.first())
    for (i in 1 until parts.size) {
        if (parts[i - 1].last().isDigit() && parts[i].first().isDigit()) {
            result.append("_")
        }
        result.append(partToUpperCamel(parts[i]))
    }
    return result.toString().let {
        when (it) {
            "class" -> "clazz"
            else -> it
        }
    }
}

fun kebabToUpperCamel(what: String) = partToUpperCamel(kebabToLowerCamel(what)).let {
    when (it) {
        "Clazz" -> "Class"
        else -> it
    }
}

fun UnionType.toKotlinType(): String {
    val options = this.options.sortedBy { it.ident }
    val uppercase = options.map { it.ident.first().isUpperCase() }.distinct().single()
    if (uppercase) {
        // Means either type variable or Kotlin own type
        return options.single() /*Sure?*/.ident
    } else {
        val res = StringBuilder()
        res.append("T")
        res.append(options.joinToString("Or") { kebabToUpperCamel(it.ident) })
        return res.toString()
    }
}

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
