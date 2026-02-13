package org.ldemetrios.tyko.compiler

import org.ldemetrios.tyko.driver.api.FlattenedSyntaxTree

data class SyntaxTree(
    val marks: List<IndexedMark>
)

data class IndexedMark(val mark: SyntaxMark, val index: Int)

sealed interface SyntaxMark {
    data class NodeStart(val kind: SyntaxKind) : SyntaxMark
    data object NodeEnd : SyntaxMark
    data class Error(val message: String) : SyntaxMark

    companion object {
        fun decode(code: Int, errorsMessage : (Int) -> String) = when (code) {
            0 -> NodeStart(SyntaxKind.End)
            1 -> NodeStart(SyntaxKind.Error)
            2 -> NodeStart(SyntaxKind.Shebang)
            3 -> NodeStart(SyntaxKind.LineComment)
            4 -> NodeStart(SyntaxKind.BlockComment)
            5 -> NodeStart(SyntaxKind.Markup)
            6 -> NodeStart(SyntaxKind.Text)
            7 -> NodeStart(SyntaxKind.Space)
            8 -> NodeStart(SyntaxKind.Linebreak)
            9 -> NodeStart(SyntaxKind.Parbreak)
            10 -> NodeStart(SyntaxKind.Escape)
            11 -> NodeStart(SyntaxKind.Shorthand)
            12 -> NodeStart(SyntaxKind.SmartQuote)
            13 -> NodeStart(SyntaxKind.Strong)
            14 -> NodeStart(SyntaxKind.Emph)
            15 -> NodeStart(SyntaxKind.Raw)
            16 -> NodeStart(SyntaxKind.RawLang)
            17 -> NodeStart(SyntaxKind.RawDelim)
            18 -> NodeStart(SyntaxKind.RawTrimmed)
            19 -> NodeStart(SyntaxKind.Link)
            20 -> NodeStart(SyntaxKind.Label)
            21 -> NodeStart(SyntaxKind.Ref)
            22 -> NodeStart(SyntaxKind.RefMarker)
            23 -> NodeStart(SyntaxKind.Heading)
            24 -> NodeStart(SyntaxKind.HeadingMarker)
            25 -> NodeStart(SyntaxKind.ListItem)
            26 -> NodeStart(SyntaxKind.ListMarker)
            27 -> NodeStart(SyntaxKind.EnumItem)
            28 -> NodeStart(SyntaxKind.EnumMarker)
            29 -> NodeStart(SyntaxKind.TermItem)
            30 -> NodeStart(SyntaxKind.TermMarker)
            31 -> NodeStart(SyntaxKind.Equation)
            32 -> NodeStart(SyntaxKind.Math)
            33 -> NodeStart(SyntaxKind.MathText)
            34 -> NodeStart(SyntaxKind.MathIdent)
            35 -> NodeStart(SyntaxKind.MathShorthand)
            36 -> NodeStart(SyntaxKind.MathAlignPoint)
            37 -> NodeStart(SyntaxKind.MathDelimited)
            38 -> NodeStart(SyntaxKind.MathAttach)
            39 -> NodeStart(SyntaxKind.MathPrimes)
            40 -> NodeStart(SyntaxKind.MathFrac)
            41 -> NodeStart(SyntaxKind.MathRoot)
            42 -> NodeStart(SyntaxKind.Hash)
            43 -> NodeStart(SyntaxKind.LeftBrace)
            44 -> NodeStart(SyntaxKind.RightBrace)
            45 -> NodeStart(SyntaxKind.LeftBracket)
            46 -> NodeStart(SyntaxKind.RightBracket)
            47 -> NodeStart(SyntaxKind.LeftParen)
            48 -> NodeStart(SyntaxKind.RightParen)
            49 -> NodeStart(SyntaxKind.Comma)
            50 -> NodeStart(SyntaxKind.Semicolon)
            51 -> NodeStart(SyntaxKind.Colon)
            52 -> NodeStart(SyntaxKind.Star)
            53 -> NodeStart(SyntaxKind.Underscore)
            54 -> NodeStart(SyntaxKind.Dollar)
            55 -> NodeStart(SyntaxKind.Plus)
            56 -> NodeStart(SyntaxKind.Minus)
            57 -> NodeStart(SyntaxKind.Slash)
            58 -> NodeStart(SyntaxKind.Hat)
            60 -> NodeStart(SyntaxKind.Dot)
            61 -> NodeStart(SyntaxKind.Eq)
            62 -> NodeStart(SyntaxKind.EqEq)
            63 -> NodeStart(SyntaxKind.ExclEq)
            64 -> NodeStart(SyntaxKind.Lt)
            65 -> NodeStart(SyntaxKind.LtEq)
            66 -> NodeStart(SyntaxKind.Gt)
            67 -> NodeStart(SyntaxKind.GtEq)
            68 -> NodeStart(SyntaxKind.PlusEq)
            69 -> NodeStart(SyntaxKind.HyphEq)
            70 -> NodeStart(SyntaxKind.StarEq)
            71 -> NodeStart(SyntaxKind.SlashEq)
            72 -> NodeStart(SyntaxKind.Dots)
            73 -> NodeStart(SyntaxKind.Arrow)
            74 -> NodeStart(SyntaxKind.Root)
            75 -> NodeStart(SyntaxKind.Bang)
            76 -> NodeStart(SyntaxKind.Not)
            77 -> NodeStart(SyntaxKind.And)
            78 -> NodeStart(SyntaxKind.Or)
            79 -> NodeStart(SyntaxKind.None)
            80 -> NodeStart(SyntaxKind.Auto)
            81 -> NodeStart(SyntaxKind.Let)
            82 -> NodeStart(SyntaxKind.Set)
            83 -> NodeStart(SyntaxKind.Show)
            84 -> NodeStart(SyntaxKind.Context)
            85 -> NodeStart(SyntaxKind.If)
            86 -> NodeStart(SyntaxKind.Else)
            87 -> NodeStart(SyntaxKind.For)
            88 -> NodeStart(SyntaxKind.In)
            89 -> NodeStart(SyntaxKind.While)
            90 -> NodeStart(SyntaxKind.Break)
            91 -> NodeStart(SyntaxKind.Continue)
            92 -> NodeStart(SyntaxKind.Return)
            93 -> NodeStart(SyntaxKind.Import)
            94 -> NodeStart(SyntaxKind.Include)
            95 -> NodeStart(SyntaxKind.As)
            96 -> NodeStart(SyntaxKind.Code)
            97 -> NodeStart(SyntaxKind.Ident)
            98 -> NodeStart(SyntaxKind.Bool)
            99 -> NodeStart(SyntaxKind.Int)
            100 -> NodeStart(SyntaxKind.Float)
            101 -> NodeStart(SyntaxKind.Numeric)
            102 -> NodeStart(SyntaxKind.Str)
            103 -> NodeStart(SyntaxKind.CodeBlock)
            104 -> NodeStart(SyntaxKind.ContentBlock)
            105 -> NodeStart(SyntaxKind.Parenthesized)
            106 -> NodeStart(SyntaxKind.Array)
            107 -> NodeStart(SyntaxKind.Dict)
            108 -> NodeStart(SyntaxKind.Named)
            109 -> NodeStart(SyntaxKind.Keyed)
            110 -> NodeStart(SyntaxKind.Unary)
            111 -> NodeStart(SyntaxKind.Binary)
            112 -> NodeStart(SyntaxKind.FieldAccess)
            113 -> NodeStart(SyntaxKind.FuncCall)
            114 -> NodeStart(SyntaxKind.Args)
            115 -> NodeStart(SyntaxKind.Spread)
            116 -> NodeStart(SyntaxKind.Closure)
            117 -> NodeStart(SyntaxKind.Params)
            118 -> NodeStart(SyntaxKind.LetBinding)
            119 -> NodeStart(SyntaxKind.SetRule)
            120 -> NodeStart(SyntaxKind.ShowRule)
            121 -> NodeStart(SyntaxKind.Contextual)
            122 -> NodeStart(SyntaxKind.Conditional)
            123 -> NodeStart(SyntaxKind.WhileLoop)
            124 -> NodeStart(SyntaxKind.ForLoop)
            125 -> NodeStart(SyntaxKind.ModuleImport)
            126 -> NodeStart(SyntaxKind.ImportItems)
            127 -> NodeStart(SyntaxKind.ImportItemPath)
            128 -> NodeStart(SyntaxKind.RenamedImportItem)
            129 -> NodeStart(SyntaxKind.ModuleInclude)
            130 -> NodeStart(SyntaxKind.LoopBreak)
            131 -> NodeStart(SyntaxKind.LoopContinue)
            132 -> NodeStart(SyntaxKind.FuncReturn)
            133 -> NodeStart(SyntaxKind.Destructuring)
            134 -> NodeStart(SyntaxKind.DestructAssignment)
            135 -> NodeEnd
            else -> Error(errorsMessage(code - 136))
        }
    }
}

enum class SyntaxKind {
    /// The end of token stream.
    End,
    /// An invalid sequence of characters.
    Error,

    /// A shebang: `#! ...`
    Shebang,
    /// A line comment: `// ...`.
    LineComment,
    /// A block comment: `/* ... */`.
    BlockComment,

    /// The contents of a file or content block.
    Markup,
    /// Plain text without markup.
    Text,
    /// Whitespace. Contains at most one newline in markup, as more indicate a
    /// paragraph break.
    Space,
    /// A forced line break: `\`.
    Linebreak,
    /// A paragraph break, indicated by one or multiple blank lines.
    Parbreak,
    /// An escape sequence: `\#`, `\u{1F5FA}`.
    Escape,
    /// A shorthand for a unicode codepoint. For example, `~` for non-breaking
    /// space or `-?` for a soft hyphen.
    Shorthand,
    /// A smart quote: `'` or `"`.
    SmartQuote,
    /// Strong content: `*Strong*`.
    Strong,
    /// Emphasized content: `_Emphasized_`.
    Emph,
    /// Raw text with optional syntax highlighting: `` `...` ``.
    Raw,
    /// A language tag at the start of raw text: ``typ ``.
    RawLang,
    /// A raw delimiter consisting of 1 or 3+ backticks: `` ` ``.
    RawDelim,
    /// A sequence of whitespace to ignore in a raw text: `    `.
    RawTrimmed,
    /// A hyperlink: `https://typst.org`.
    Link,
    /// A label: `<intro>`.
    Label,
    /// A reference: `@target`, `@target[..]`.
    Ref,
    /// Introduces a reference: `@target`.
    RefMarker,
    /// A section heading: `= Introduction`.
    Heading,
    /// Introduces a section heading: `=`, `==`, ...
    HeadingMarker,
    /// An item in a bullet list: `- ...`.
    ListItem,
    /// Introduces a list item: `-`.
    ListMarker,
    /// An item in an enumeration (numbered list): `+ ...` or `1. ...`.
    EnumItem,
    /// Introduces an enumeration item: `+`, `1.`.
    EnumMarker,
    /// An item in a term list: `/ Term: Details`.
    TermItem,
    /// Introduces a term item: `/`.
    TermMarker,
    /// A mathematical equation: `$x$`, `$ x^2 $`.
    Equation,

    /// The contents of a mathematical equation: `x^2 + 1`.
    Math,
    /// A lone text fragment in math: `x`, `25`, `3.1415`, `=`, `|`, `[`.
    MathText,
    /// An identifier in math: `pi`.
    MathIdent,
    /// A shorthand for a unicode codepoint in math: `a <= b`.
    MathShorthand,
    /// An alignment point in math: `&`.
    MathAlignPoint,
    /// Matched delimiters in math: `[x + y]`.
    MathDelimited,
    /// A base with optional attachments in math: `a_1^2`.
    MathAttach,
    /// Grouped primes in math: `a'''`.
    MathPrimes,
    /// A fraction in math: `x/2`.
    MathFrac,
    /// A root in math: `√x`, `∛x` or `∜x`.
    MathRoot,

    /// A hash that switches into code mode: `#`.
    Hash,
    /// A left curly brace, starting a code block: `{`.
    LeftBrace,
    /// A right curly brace, terminating a code block: `}`.
    RightBrace,
    /// A left square bracket, starting a content block: `[`.
    LeftBracket,
    /// A right square bracket, terminating a content block: `]`.
    RightBracket,
    /// A left round parenthesis, starting a grouped expression, collection,
    /// argument or parameter list: `(`.
    LeftParen,
    /// A right round parenthesis, terminating a grouped expression, collection,
    /// argument or parameter list: `)`.
    RightParen,
    /// A comma separator in a sequence: `,`.
    Comma,
    /// A semicolon terminating an expression: `;`.
    Semicolon,
    /// A colon between name/key and value in a dictionary, argument or
    /// parameter list, or between the term and body of a term list term: `:`.
    Colon,
    /// The strong text toggle, multiplication operator, and wildcard import
    /// symbol: `*`.
    Star,
    /// Toggles emphasized text and indicates a subscript in math: `_`.
    Underscore,
    /// Starts and ends a mathematical equation: `$`.
    Dollar,
    /// The unary plus and binary addition operator: `+`.
    Plus,
    /// The unary negation and binary subtraction operator: `-`.
    Minus,
    /// The division operator and fraction operator in math: `/`.
    Slash,
    /// The superscript operator in math: `^`.
    Hat,
    /// The field access and method call operator: `.`.
    Dot,
    /// The assignment operator: `=`.
    Eq,
    /// The equality operator: `==`.
    EqEq,
    /// The inequality operator: `!=`.
    ExclEq,
    /// The less-than operator: `<`.
    Lt,
    /// The less-than or equal operator: `<=`.
    LtEq,
    /// The greater-than operator: `>`.
    Gt,
    /// The greater-than or equal operator: `>=`.
    GtEq,
    /// The add-assign operator: `+=`.
    PlusEq,
    /// The subtract-assign operator: `-=`.
    HyphEq,
    /// The multiply-assign operator: `*=`.
    StarEq,
    /// The divide-assign operator: `/=`.
    SlashEq,
    /// Indicates a spread or sink: `..`.
    Dots,
    /// An arrow between a closure's parameters and body: `=>`.
    Arrow,
    /// A root: `√`, `∛` or `∜`.
    Root,
    /// An exclamation mark; groups with directly preceding text in math: `!`.
    Bang,

    /// The `not` operator.
    Not,
    /// The `and` operator.
    And,
    /// The `or` operator.
    Or,
    /// The `none` literal.
    None,
    /// The `auto` literal.
    Auto,
    /// The `let` keyword.
    Let,
    /// The `set` keyword.
    Set,
    /// The `show` keyword.
    Show,
    /// The `context` keyword.
    Context,
    /// The `if` keyword.
    If,
    /// The `else` keyword.
    Else,
    /// The `for` keyword.
    For,
    /// The `in` keyword.
    In,
    /// The `while` keyword.
    While,
    /// The `break` keyword.
    Break,
    /// The `continue` keyword.
    Continue,
    /// The `return` keyword.
    Return,
    /// The `import` keyword.
    Import,
    /// The `include` keyword.
    Include,
    /// The `as` keyword.
    As,

    /// The contents of a code block.
    Code,
    /// An identifier: `it`.
    Ident,
    /// A boolean: `true`, `false`.
    Bool,
    /// An integer: `120`.
    Int,
    /// A floating-point number: `1.2`, `10e-4`.
    Float,
    /// A numeric value with a unit: `12pt`, `3cm`, `2em`, `90deg`, `50%`.
    Numeric,
    /// A quoted string: `"..."`.
    Str,
    /// A code block: `{ let x = 1; x + 2 }`.
    CodeBlock,
    /// A content block: `[*Hi* there!]`.
    ContentBlock,
    /// A grouped expression: `(1 + 2)`.
    Parenthesized,
    /// An array: `(1, "hi", 12cm)`.
    Array,
    /// A dictionary: `(thickness: 3pt, dash: "solid")`.
    Dict,
    /// A named pair: `thickness: 3pt`.
    Named,
    /// A keyed pair: `"spacy key": true`.
    Keyed,
    /// A unary operation: `-x`.
    Unary,
    /// A binary operation: `a + b`.
    Binary,
    /// A field access: `properties.age`.
    FieldAccess,
    /// An invocation of a function or method: `f(x, y)`.
    FuncCall,
    /// A function call's argument list: `(12pt, y)`.
    Args,
    /// Spread arguments or an argument sink: `..x`.
    Spread,
    /// A closure: `(x, y) => z`.
    Closure,
    /// A closure's parameters: `(x, y)`.
    Params,
    /// A let binding: `let x = 1`.
    LetBinding,
    /// A set rule: `set text(...)`.
    SetRule,
    /// A show rule: `show heading: it => emph(it.body)`.
    ShowRule,
    /// A contextual expression: `context text.lang`.
    Contextual,
    /// An if-else conditional: `if x { y } else { z }`.
    Conditional,
    /// A while loop: `while x { y }`.
    WhileLoop,
    /// A for loop: `for x in y { z }`.
    ForLoop,
    /// A module import: `import "utils.typ": a, b, c`.
    ModuleImport,
    /// Items to import from a module: `a, b, c`.
    ImportItems,
    /// A path to an imported name from a submodule: `a.b.c`.
    ImportItemPath,
    /// A renamed import item: `a as d`.
    RenamedImportItem,
    /// A module include: `include "chapter1.typ"`.
    ModuleInclude,
    /// A break from a loop: `break`.
    LoopBreak,
    /// A continue in a loop: `continue`.
    LoopContinue,
    /// A return from a function: `return`, `return x + 1`.
    FuncReturn,
    /// A destructuring pattern: `(x, _, ..y)`.
    Destructuring,
    /// A destructuring assignment expression: `(x, y) = (1, 2)`.
    DestructAssignment;
}

private fun FlattenedSyntaxTree.decodeErrors(): List<String> {
    if (errorStarts.isEmpty()) return emptyList()
    val messages = ArrayList<String>(errorStarts.size)
    for (i in errorStarts.indices) {
        val start = errorStarts[i]
        val end = if (i + 1 < errorStarts.size) errorStarts[i + 1] else errors.size
        if (start < 0 || end < start || start > errors.size) {
            messages.add("")
            continue
        }
        val safeEnd = end.coerceAtMost(errors.size)
        val slice = errors.copyOfRange(start, safeEnd)
        messages.add(String(slice, Charsets.UTF_8))
    }
    return messages
}

private fun buildUtf8ByteToCharIndexMap(source: String): IntArray {
    val bytes = source.toByteArray(Charsets.UTF_8)
    val byteToChar = IntArray(bytes.size + 1)
    var bytePos = 0
    var charPos = 0
    while (charPos < source.length) {
        val codePoint = source.codePointAt(charPos)
        val charCount = Character.charCount(codePoint)
        val byteLen = String(Character.toChars(codePoint)).toByteArray(Charsets.UTF_8).size
        val nextBytePos = (bytePos + byteLen).coerceAtMost(bytes.size)
        byteToChar[bytePos] = charPos
        if (bytePos + 1 < nextBytePos) {
            for (i in (bytePos + 1) until nextBytePos) {
                byteToChar[i] = charPos
            }
        }
        byteToChar[nextBytePos] = (charPos + charCount).coerceAtMost(source.length)
        bytePos = nextBytePos
        charPos += charCount
    }
    if (bytePos <= bytes.size) {
        for (i in bytePos..bytes.size) {
            byteToChar[i] = charPos
        }
    }
    return byteToChar
}

internal fun FlattenedSyntaxTree.toSyntaxTree(source: String): SyntaxTree {
    val errorMessages = decodeErrors()
    val byteToChar = buildUtf8ByteToCharIndexMap(source)
    val marks = marks.map { encoded ->
        val code = (encoded ushr 32).toInt()
        val rawIndex = (encoded and 0xFFFF_FFFFL).toInt()
        val index = when {
            rawIndex < 0 -> 0
            rawIndex >= byteToChar.size -> source.length
            else -> byteToChar[rawIndex]
        }
        val mark = SyntaxMark.decode(code) { errorMessages[it] }
        IndexedMark(mark, index)
    }
    return SyntaxTree(marks)
}
