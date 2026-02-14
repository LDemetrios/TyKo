package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/text/sub/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/text/sub/](https://typst.app/docs/reference/text/sub/)
 * 
 * Renders text in subscript.
 * 
 * The text is rendered smaller and its baseline is lowered.
 * 
 * **_Example_**
 * 
 * ```typ
 * Revenue#sub[yearly]
 * ```
 * <img src="https://typst.app/assets/docs/q6m3B3bVOLKPuJFIogqIMwAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("sub")
data class TSub(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/sub/](https://typst.app/docs/reference/text/sub/)
     * 
     * The text to display in subscript.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/sub/](https://typst.app/docs/reference/text/sub/)
     * 
     * Whether to use subscript glyphs from the font if available.
     * 
     * Ideally, subscripts glyphs are provided by the font (using the `subs` OpenType feature). Otherwise, Typst is able to synthesize subscripts by lowering and scaling down regular glyphs.
     * 
     * When this is set to `false`, synthesized glyphs will be used regardless of whether the font provides dedicated subscript glyphs. When `true`, synthesized glyphs may still be used in case the font does not provide the necessary subscript glyphs.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val typographic: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/sub/](https://typst.app/docs/reference/text/sub/)
     * 
     * The downward baseline shift for synthesized subscripts.
     * 
     * This only applies to synthesized subscripts. In other words, this has no effect if `typographic` is `true` and the font provides the necessary subscript glyphs.
     * 
     * If set to `auto`, the baseline is shifted according to the metrics provided by the font, with a fallback to `0.2em` in case the font does not define the necessary metrics.
     * 
     * Settable; Typst type: auto|length
     */
    @all:Settable val baseline: Smart<TLength>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/sub/](https://typst.app/docs/reference/text/sub/)
     * 
     * The font size for synthesized subscripts.
     * 
     * This only applies to synthesized subscripts. In other words, this has no effect if `typographic` is `true` and the font provides the necessary subscript glyphs.
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
        val ELEM = TElement("sub")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TSub]
 */
@SerialName("set-sub")
data class TSetSub(
    override val internals: SetRuleInternals? = null,
    val typographic: TBool? = null,
    val baseline: Smart<TLength>? = null,
    val size: Smart<TLength>? = null
) : TSetRule()
