package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/text/super/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/text/super/](https://typst.app/docs/reference/text/super/)
 * 
 * Renders text in superscript.
 * 
 * The text is rendered smaller and its baseline is raised.
 * 
 * **_Example_**
 * 
 * ```typ
 * 1#super[st] try!
 * ```
 * <img src="https://typst.app/assets/docs/052zwKrkvVHtZVzW65WFdQAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("super")
data class TSuper(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/super/](https://typst.app/docs/reference/text/super/)
     * 
     * The text to display in superscript.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/super/](https://typst.app/docs/reference/text/super/)
     * 
     * Whether to use superscript glyphs from the font if available.
     * 
     * Ideally, superscripts glyphs are provided by the font (using the `sups` OpenType feature). Otherwise, Typst is able to synthesize superscripts by raising and scaling down regular glyphs.
     * 
     * When this is set to `false`, synthesized glyphs will be used regardless of whether the font provides dedicated superscript glyphs. When `true`, synthesized glyphs may still be used in case the font does not provide the necessary superscript glyphs.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val typographic: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/super/](https://typst.app/docs/reference/text/super/)
     * 
     * The downward baseline shift for synthesized superscripts.
     * 
     * This only applies to synthesized superscripts. In other words, this has no effect if `typographic` is `true` and the font provides the necessary superscript glyphs.
     * 
     * If set to `auto`, the baseline is shifted according to the metrics provided by the font, with a fallback to `-0.5em` in case the font does not define the necessary metrics.
     * 
     * Note that, since the baseline shift is applied downward, you will need to provide a negative value for the content to appear as raised above the normal baseline.
     * 
     * Settable; Typst type: auto|length
     */
    @all:Settable val baseline: Smart<TLength>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/super/](https://typst.app/docs/reference/text/super/)
     * 
     * The font size for synthesized superscripts.
     * 
     * This only applies to synthesized superscripts. In other words, this has no effect if `typographic` is `true` and the font provides the necessary superscript glyphs.
     * 
     * If set to `auto`, the size is scaled according to the metrics provided by the font, with a fallback to `0.6em` in case the font does not define the necessary metrics.
     * 
     * Settable; Typst type: auto|length
     */
    @all:Settable val size: Smart<TLength>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("super")
    }
}


@SerialName("set-super")
data class TSetSuper(
    override val internals: SetRuleInternals? = null,
    val typographic: TBool? = null,
    val baseline: Smart<TLength>? = null,
    val size: Smart<TLength>? = null
) : TSetRule()
