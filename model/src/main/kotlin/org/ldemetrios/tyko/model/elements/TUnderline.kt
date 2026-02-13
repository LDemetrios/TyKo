package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/text/underline/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/text/underline/](https://typst.app/docs/reference/text/underline/)
 * 
 * Underlines text.
 * 
 * **_Example_**
 * 
 * ```typ
 * This is #underline[important].
 * ```
 * <img src="https://typst.app/assets/docs/xV-Fy8zwdVIfyHyOpdk_9AAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("underline")
data class TUnderline(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/underline/](https://typst.app/docs/reference/text/underline/)
     * 
     * The content to underline.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/underline/](https://typst.app/docs/reference/text/underline/)
     * 
     * How to [stroke](https://typst.app/docs/reference/visualize/stroke/) the line.
     * 
     * If set to `auto`, takes on the text's color and a thickness defined in the current font.
     * 
     * Settable; Typst type: auto|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Smart<TStroke>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/underline/](https://typst.app/docs/reference/text/underline/)
     * 
     * The position of the line relative to the baseline, read from the font tables if `auto`.
     * 
     * Settable; Typst type: auto|length
     */
    @all:Settable val offset: Smart<TLength>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/underline/](https://typst.app/docs/reference/text/underline/)
     * 
     * The amount by which to extend the line beyond (or within if negative) the content.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val extent: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/underline/](https://typst.app/docs/reference/text/underline/)
     * 
     * Whether the line skips sections in which it would collide with the glyphs.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val evade: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/underline/](https://typst.app/docs/reference/text/underline/)
     * 
     * Whether the line is placed behind the content it underlines.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val background: TBool? = null,
    override val label: TLabel? = null
) : TSelectableContent<TUnderline>() {
    override fun elem(): TLocatableElement<TUnderline> = ELEM

    companion object {
        val ELEM = TLocatableElement<TUnderline>("underline")
    }
}


@SerialName("set-underline")
data class TSetUnderline(
    override val internals: SetRuleInternals? = null,
    val stroke: Smart<TStroke>? = null,
    val offset: Smart<TLength>? = null,
    val extent: TLength? = null,
    val evade: TBool? = null,
    val background: TBool? = null
) : TSetRule()
