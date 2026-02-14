package org.ldemetrios.tyko.model



//!https://typst.app/docs/reference/text/smartquote/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/text/smartquote/](https://typst.app/docs/reference/text/smartquote/)
 * 
 * A language-aware quote that reacts to its context.
 * 
 * Automatically turns into an appropriate opening or closing quote based on the active [text language](https://typst.app/docs/reference/text/text/#parameters-lang).
 * 
 * **_Example_**
 * 
 * ```typ
 * "This is in quotes."
 * 
 * #set text(lang: "de")
 * "Das ist in Anführungszeichen."
 * 
 * #set text(lang: "fr")
 * "C'est entre guillemets."
 * ```
 * <img src="https://typst.app/assets/docs/dhrUjSwC3cH8VIWvplWrzwAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This function also has dedicated syntax: The normal quote characters (`'` and `"`). Typst automatically makes your quotes smart.
 */
@SerialName("smartquote")
data class TSmartquote(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/smartquote/](https://typst.app/docs/reference/text/smartquote/)
     * 
     * Whether this should be a double quote.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val double: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/smartquote/](https://typst.app/docs/reference/text/smartquote/)
     * 
     * Whether smart quotes are enabled.
     * 
     * To disable smartness for a single quote, you can also escape it with a backslash.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val enabled: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/smartquote/](https://typst.app/docs/reference/text/smartquote/)
     * 
     * Whether to use alternative quotes.
     * 
     * Does nothing for languages that don't have alternative quotes, or if explicit quotes were set.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val alternative: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/smartquote/](https://typst.app/docs/reference/text/smartquote/)
     * 
     * The quotes to use.
     * 
     * - When set to `auto`, the appropriate single quotes for the [text language](https://typst.app/docs/reference/text/text/#parameters-lang) will be used. This is the default.
     * - Custom quotes can be passed as a string, array, or dictionary of either
     *   - [string](https://typst.app/docs/reference/foundations/str/): a string consisting of two characters containing the opening and closing double quotes (characters here refer to Unicode grapheme clusters)
     *   - [array](https://typst.app/docs/reference/foundations/array/): an array containing the opening and closing double quotes
     *   - [dictionary](https://typst.app/docs/reference/foundations/dictionary/): a dictionary containing the double and single quotes, each specified as either `auto`, string, or array
     * 
     * Settable; Typst type: auto|str|array|dictionary
     */
    @all:Settable val quotes: TSmartquoteQuotes? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("smartquote")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TSmartquote]
 */
@SerialName("set-smartquote")
data class TSetSmartquote(
    override val internals: SetRuleInternals? = null,
    val double: TBool? = null,
    val enabled: TBool? = null,
    val alternative: TBool? = null,
    val quotes: TSmartquoteQuotes? = null
) : TSetRule()
