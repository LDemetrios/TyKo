package parsers

import java.io.File

data class TypeParameter(val param: Type, val variance: String) {
    override fun toString(): String = if (variance == "") "$param" else "$variance $param"
}

sealed interface Type {
    fun isGeneric(): Boolean
}

data class ConcreteType(val name: String, val params: List<TypeParameter> = listOf()) : Type {
    override fun toString(): String = if (params.isEmpty()) name else "$name<${params.joinToString(", ")}>"
    override fun isGeneric(): Boolean = params.isNotEmpty()
}

data class UnionType(val variants: List<Type>) : Type {
    override fun toString(): String = variants.joinToString(" | ")
    fun flatten(): UnionType {
        val variants = mutableListOf<Type>()
        for (variant in this.variants) {
            when (variant) {
                is UnionType -> variants.addAll(variant.flatten().variants)
                else -> variants.add(variant)
            }
        }
        return UnionType(variants)
    }

    fun flattenSingle(): Type = when {
        variants.size == 1 -> variants[0]
        variants.any { it is AnyType } -> AnyType
        else -> this
    }

    override fun isGeneric(): Boolean = variants.any { it.isGeneric() }
}

data class Primitive(
    val type: ConcreteType,
    val native: ConcreteType,
    val delegatable: Boolean,
    val annotations: List<String>
) {
    override fun toString(): String = "$type -> $native" + if (delegatable) " (delegatable)" else ""
}

data object AnyType : Type {
    override fun toString(): String = "*"
    override fun isGeneric(): Boolean = false
}

data class ClassElement(
    val name: String,
    val type: Type,
    val varargs: Boolean,
    val required: Boolean,
    val positional: Boolean,
    val annotations: List<String>
) {
    override fun toString(): String =
        "$name: $type" + (if (required) " (req)" else "") + (if (positional) " (pos)" else "")
}

data class ClassDefinition(val type: ConcreteType, val classElements: List<ClassElement>, val parent: ConcreteType?) {
    override fun toString(): String =
        "$type {\n" + classElements.joinToString("\n") { "    $it" } + "\n}" + (if (parent != null) " -> $parent" else "")
}

fun main() {
    val was = File("/home/ldemetrios/Workspace/Typst4k/others").readText()
    val clean = CommentsRemover(was).also { it.parse() }.result.toString()
    val parser = ModelParser(clean)
    parser.parse()
    println(parser.primitives)
    println(parser.classDefinitions)
    println(parser.specformat)
    println(parser.ignore)
}

class ModelParser(data: String) : BaseParser(data) {
    val primitives = mutableMapOf<String, Primitive>()
    val specformat = mutableListOf<String>()
    val deprecated = mutableListOf<String>()
    val ignore = mutableListOf<String>()
    val classDefinitions = mutableMapOf<String, ClassDefinition>()

    override fun lookup(value: String): Boolean {
        skipWhitespace()
        return super.lookup(value).also { skipWhitespace() }
    }

    fun parse() {
        while (!eof()) {
            if (lookup("specformat")) {
                specformat.addAll(lineRemainder())
            } else if (lookup("ignore")) {
                ignore.addAll(lineRemainder())
            } else if (lookup("primitive")) {
                val pr = parsePrimitive()
                primitives[pr.type.name] = pr
            } else if (lookup("class")) {
                val cl = parseClass()
                classDefinitions[cl.type.name] = cl
            } else if (lookup("deprecate")){
                deprecated.addAll(lineRemainder())
            } else {
                throw error("Unknown token: ${this.toString().replace("\n", " ")}")
            }
        }
    }

    private fun parseClass(): ClassDefinition {
        val type = parseConcreteType()
        skipWhitespace()
        expect("(")
        val classElements = mutableListOf<ClassElement>()
        while (!lookup(")")) {
            var (required, positional, varargs) = BooleanArray(3)
            repeat(3) { // I don't really care
                required = required || lookup("req ")
                positional = positional || lookup("pos ")
                varargs = varargs || lookup("... ")
            }
            val name = parseIdentifier()
            skipWhitespace()
            expect(":")
            val rawUnion = parseUnionType().run {
                if (this is UnionType) flatten().flattenSingle()
                else this
            }
            val annotations = parseAnnotations()
            lookup(",")
            classElements.add(ClassElement(name, rawUnion, varargs, required, positional, annotations))
        }
        val parent = if (lookup("->")) {
            parseConcreteType()
        } else null
        return ClassDefinition(type, classElements, parent)
    }

    private fun parseUnionType(): Type {
        if (lookup("*")) return AnyType
        val variants = mutableListOf<Type>(parseConcreteType())
        while (lookup("|")) {
            variants.add(parseConcreteType())
        }
        val (flattened) = UnionType(variants).flatten()
        return UnionType(flattened.filter { it !is ConcreteType || it.name !in ignore })
    }

    private fun parsePrimitive(): Primitive {
        val type = parseConcreteType()
        if (!lookup("=")) throw AssertionError()
        val delegatable = lookup("interface")
        val native = parseConcreteType()
        val annotations = parseAnnotations()
        return Primitive(type, native, delegatable, annotations)
    }

    private fun parseAnnotations(): List<String> {
        val annotations = mutableListOf<String>()
        while (lookup("@")) {
            val sb = StringBuilder("@")
            sb.append(parseIdentifier())
            if (lookup("(")) {
                sb.append("(")
                parseParenthesisTo(sb, ')')
            }
            annotations.add(sb.toString())
        }
        return annotations
    }

    private fun parseParenthesisTo(sb: StringBuilder, s: Char) {
        while (!test(s)) {
            val next = take()
            sb.append(next)
            when (next) {
                '(' -> parseParenthesisTo(sb, ')')
                '[' -> parseParenthesisTo(sb, ']')
                '{' -> parseParenthesisTo(sb, '}')
                '<' -> parseParenthesisTo(sb, '>')
                '"' -> parseStringTo(sb)
            }
        }
        sb.append(take())
    }

    private fun parseStringTo(sb: StringBuilder) {
        while (!test('"')) {
            if (peek() == '\\') {
                sb.append(take())
                sb.append(take())
            } else {
                sb.append(take())
            }
        }
        sb.append(take())
    }

    private fun lineRemainder(): List<String> {
        val sb = StringBuilder()
        while (!eof() && !take('\n') && !take('\r')) {
            sb.append(take())
        }
        return sb.split(" ").map { it.trim() }.filter { it.isNotBlank() }
    }

    private fun parseConcreteType(): ConcreteType {
        val ident = parseIdentifier()
        val generic = lookup("<")
        val params = if (generic) {
            val params = mutableListOf<TypeParameter>()
            while (!lookup(">")) {
                params.add(parseTypeParameter())
                lookup(",")
            }
            params
        } else listOf()
        return ConcreteType(ident, params)
    }

    private fun parseTypeParameter(): TypeParameter {
        if (lookup("*")) return TypeParameter(AnyType, "")
        val variance = if (lookup("out ")) "out" else if (lookup("in ")) "in" else ""
        val param = parseUnionType()
        return TypeParameter(param.run {
            if (this is UnionType) flatten().flattenSingle()
            else this
        }, variance)
    }

    private fun parseIdentifier(): String {
        skipWhitespace()
        val sb = StringBuilder()
        while (test { "$it".matches(Regex("[a-zA-Z\\-.]")) }) {
            sb.append(take())
        }
        return sb.toString()

    }
}

class CommentsRemover(data: String) : BaseParser(data) {
    val result = StringBuilder()

    fun parse() {
        while (!eof()) {
            if (take('/')) {
                if (!eof() && take(/*another*/ '/')) {
                    while (!take('\n')) take()
                    result.append('\n')
                } else if (!eof() && take('*')) {
                    blockCommentUntilEnd()
                } else {
                    result.append('/')
                }
            } else result.append(take())
        }
    }

    private fun blockCommentUntilEnd() {
        while (!eof()) {
            if (take('*')) {
                if (!eof() && take('/')) {
                    return
                }
            } else if (take('/')) {
                if (!eof() && take('*')) {
                    blockCommentUntilEnd()
                }
            } else take()
        }
    }
}