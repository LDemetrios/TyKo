package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/text/linebreak/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/text/linebreak/](https://typst.app/docs/reference/text/linebreak/)
 * 
 * Inserts a line break.
 * 
 * Advances the paragraph to the next line. A single trailing line break at the end of a paragraph is ignored, but more than one creates additional empty lines.
 * 
 * **_Example_**
 * 
 * ```typ
 * *Date:* 26.12.2022 \
 * *Topic:* Infrastructure Test \
 * *Severity:* High \
 * ```
 * <img src="https://typst.app/assets/docs/OEyyibskK4bIsTh7Qcp7OAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This function also has dedicated syntax: To insert a line break, simply write a backslash followed by whitespace. This always creates an unjustified break.
 */
@SerialName("linebreak")
data class TLinebreak(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/linebreak/](https://typst.app/docs/reference/text/linebreak/)
     * 
     * Whether to justify the line before the break.
     * 
     * This is useful if you found a better line break opportunity in your justified text than Typst did.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val justify: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("linebreak")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TLinebreak]
 */
@SerialName("set-linebreak")
data class TSetLinebreak(
    override val internals: SetRuleInternals? = null,
    val justify: TBool? = null
) : TSetRule()
