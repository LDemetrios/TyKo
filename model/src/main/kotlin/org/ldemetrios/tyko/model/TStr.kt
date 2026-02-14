package org.ldemetrios.tyko.model


import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.ldemetrios.tyko.model.Numbering
import org.ldemetrios.tyko.model.TAttachment
import org.ldemetrios.tyko.model.TSymbolLike

//!https://typst.app/docs/reference/foundations/str/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/str/](https://typst.app/docs/reference/foundations/str/)
 * 
 * A sequence of Unicode codepoints.
 * 
 * You can iterate over the grapheme clusters of the string using a [for loop](https://typst.app/docs/reference/scripting/#loops). Grapheme clusters are basically characters but keep together things that belong together, e.g. multiple codepoints that together form a flag emoji. Strings can be added with the `+` operator, [joined together](https://typst.app/docs/reference/scripting/#blocks) and multiplied with integers.
 * 
 * Typst provides utility methods for string manipulation. Many of these methods (e.g., [`split`](https://typst.app/docs/reference/foundations/str/#definitions-split), [`trim`](https://typst.app/docs/reference/foundations/str/#definitions-trim) and [`replace`](https://typst.app/docs/reference/foundations/str/#definitions-replace)) operate on *patterns:* A pattern can be either a string or a [regular expression](https://typst.app/docs/reference/foundations/regex/). This makes the methods quite versatile.
 * 
 * All lengths and indices are expressed in terms of UTF-8 bytes. Indices are zero-based and negative indices wrap around to the end of the string.
 * 
 * You can convert a value to a string with this type's constructor.
 * 
 * **_Example_**
 * 
 * ```typ
 * #"hello world!" \
 * #"\"hello\n  world\"!" \
 * #"1 2 3".split() \
 * #"1,2;3".split(regex("[,;]")) \
 * #(regex("\\d+") in "ten euros") \
 * #(regex("\\d+") in "10 euros")
 * ```
 * <img src="https://typst.app/assets/docs/uDo7SR0aD45mNBtl2tpN-AAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Escape sequences_**
 * 
 * Just like in markup, you can escape a few symbols in strings:
 * 
 * - `\\` for a backslash
 * - `\"` for a quote
 * - `\n` for a newline
 * - `\r` for a carriage return
 * - `\t` for a tab
 * - `\u{1f600}` for a hexadecimal Unicode escape sequence
 */
@SerialName("str")
data class TStr(val value: String) : TValue, TSmartquoteLevel, TFigureKind, TFontDescriptor, Smart<TStr>, TAttachment, TCounterKey,
    TSymbolLike, Option<TStr>, ArrayOrSingle<TStr>, Numbering, TLinkDestination {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.STR
    }

    override fun repr(sb: StringBuilder) {
        sb.append(Json.encodeToString(value))
    }
}

/**
 * Convenience extension converting `String` into the corresponding Typst value.
 */
val String.t get() = TStr(this)
