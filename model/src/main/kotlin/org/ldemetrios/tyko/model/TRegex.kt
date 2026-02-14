package org.ldemetrios.tyko.model


//!https://typst.app/docs/reference/foundations/regex/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/regex/](https://typst.app/docs/reference/foundations/regex/)
 * 
 * A regular expression.
 * 
 * Can be used as a [show rule selector](https://typst.app/docs/reference/styling/#show-rules) and with [string methods](https://typst.app/docs/reference/foundations/str/) like `find`, `split`, and `replace`.
 * 
 * [See here](https://docs.rs/regex/latest/regex/#syntax) for a specification of the supported syntax.
 * 
 * **_Example_**
 * 
 * ```typ
 * // Works with string methods.
 * #"a,b;c".split(regex("[,;]"))
 * 
 * // Works with show rules.
 * #show regex("\\d+"): set text(red)
 * 
 * The numbers 1 to 10.
 * ```
 * <img src="https://typst.app/assets/docs/ONri7kfbqa_s5kwQxCqv-AAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("regex")
data class TRegex(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/foundations/regex/](https://typst.app/docs/reference/foundations/regex/)
     * 
     * The regular expression as a string.
     * 
     * Both Typst strings and regular expressions use backslashes for escaping. To produce a regex escape sequence that is also valid in Typst, you need to escape the backslash itself (e.g., writing `regex("\\\\")` for the regex `\\`). Regex escape sequences that are not valid Typst escape sequences (e.g., `\d` and `\b`) can be entered into strings directly, but it's good practice to still escape them to avoid ambiguity (i.e., `regex("\\b\\d")`). See the [list of valid string escape sequences](https://typst.app/docs/reference/foundations/str/#escapes).
     * 
     * If you need many escape sequences, you can also create a raw element and extract its text to use it for your regular expressions: `regex(`\d+\.\d+\.\d+`.text)`.
     * 
     * Required, positional; Typst type: str
     */
    @all:Positional val regex: TStr
) : TValue, TFontCoverage, Smart<TRegex>, TSelectable<TValue> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.REGEX
    }
}
