package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/text/strike/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Strikes through text.
 * 
 * **_Example_**
 * 
 * ```typ
 * This is #strike[not] relevant.
 * ```
 * <img src="https://typst.app/assets/docs/gYmGRzTLJUGSNzHzEZFB3gAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("strike")
data class TStrike(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content to strike through.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * How to [stroke](https://typst.app/docs/reference/visualize/stroke/) the line.
     * 
     * If set to `auto`, takes on the text's color and a thickness defined in the current font.
     * 
     * *Note:* Please don't use this for real redaction as you can still copy paste the text.
     * 
     * Settable; Typst type: auto|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Smart<TStroke>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The position of the line relative to the baseline. Read from the font tables if `auto`.
     * 
     * This is useful if you are unhappy with the offset your font provides.
     * 
     * Settable; Typst type: auto|length
     */
    @all:Settable val offset: Smart<TLength>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The amount by which to extend the line beyond (or within if negative) the content.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val extent: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether the line is placed behind the content.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val background: TBool? = null,
    override val label: TLabel? = null
) : TSelectableContent<TStrike>() {
    override fun elem(): TLocatableElement<TStrike> = ELEM

    companion object {
        val ELEM = TLocatableElement<TStrike>("strike")
    }
}


@SerialName("set-strike")
data class TSetStrike(
    override val internals: SetRuleInternals? = null,
    val stroke: Smart<TStroke>? = null,
    val offset: Smart<TLength>? = null,
    val extent: TLength? = null,
    val background: TBool? = null
) : TSetRule()
