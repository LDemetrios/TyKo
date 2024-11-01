package newborn

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser

fun List<ClassElem>.positionalFirst() = sortedBy { if (it.positional) 0 else 1 }

val modelParser = object : Grammar<List<Declaration>>() {
    val lpar by literalToken("(")
    val rpar by literalToken(")")
    val arrow by literalToken("->")
    val comma by literalToken(",")
    val colon by literalToken(":")
    val bar by literalToken("|")
    val lbr by literalToken("<")
    val rbr by literalToken(">")
    val star by literalToken("*")
    val eq by literalToken("=")

    val modifier by regexToken("(req|pos|set|var|sprepr(set)?|abstract|open|spser|hidden|)\\b(?![-.])")
    val declType by regexToken("(class|type|element|synthetic)\\b(?![-.])")
    val enum by regexToken("enum(?![-.])")
    val primitive by regexToken("primitive(?![-.])")
    val interf by regexToken("interface(?![-.])")
    val variance by regexToken("(out|in)\\b(?![-.])")
    val word by regexToken("[a-zA-Z\\p{L}\\-.*0-9]+(?![-.])")

    val blockComment by regexToken("/\\*([^*]|\\*(?=[^/]))*\\*/", ignore = true)
    val ws by regexToken("\\s+", ignore = true)
    val lineComment by regexToken("//[^\n\r]*", ignore = true)

    val ident by (modifier or declType or enum or primitive or interf or variance or word) use
            { text }

    val typeOption = parser(this::type) or (star use { Type("*", listOf()) })
    val unionType: Parser<UnionType> by separated(typeOption, bar, acceptZero = false) use { UnionType(terms) }

    val type by ident * optional(-lbr * separatedTerms(unionType, comma) * -rbr) use {
        Type(this.t1, this.t2)
    }

    val modifiers by zeroOrMore(modifier use { text })

    val classElem by modifiers * ident * -colon * unionType use {
        ClassElem(t1, t2, t3)
    }

    val typeParam by optional(variance) use { this?.text?.let { "$it " } ?: "" } and ident use { TypeParam(t1, t2) }

    val typeParams =
        optional(-lbr * separatedTerms(typeParam, comma) * -optional(comma) * -rbr) use { this ?: listOf() }

    val enumDeclaration =
        modifiers * -enum * ident * -lpar * separatedTerms(ident, comma) * -optional(comma) * -rpar use {
            EnumDeclaration(t1, t2, t3)
        }

    val primitiveDeclaration by modifiers * -primitive * ident * typeParams *
            -eq * optional(interf).use { this != null } * type use {
        PrimitiveDeclaration(t1, t2, t3, t4, t5)
    }

    val typeDeclaration by modifiers * declType * ident * typeParams * -lpar *
            separatedTerms(
                classElem,
                comma,
                acceptZero = true
            ) * -optional(comma) * -rpar * optional(-arrow * type) use {
        TypeDeclaration(t1, t2.text, t3, t4, t5.positionalFirst(), t6)
    }

    val declaration = enumDeclaration or primitiveDeclaration or typeDeclaration

    val file: Parser<List<Declaration>> by zeroOrMore(declaration)

    override val rootParser by file // enumDeclaration
}
