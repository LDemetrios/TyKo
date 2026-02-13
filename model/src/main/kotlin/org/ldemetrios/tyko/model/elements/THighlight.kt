package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/text/highlight/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/text/highlight/](https://typst.app/docs/reference/text/highlight/)
 * 
 * Highlights text with a background color.
 * 
 * **_Example_**
 * 
 * ```typ
 * This is #highlight[important].
 * ```
 * <img src="https://typst.app/assets/docs/QtpA6ir9UWFHeXPRr2gD9AAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("highlight")
data class THighlight(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/highlight/](https://typst.app/docs/reference/text/highlight/)
     * 
     * The content that should be highlighted.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/highlight/](https://typst.app/docs/reference/text/highlight/)
     * 
     * The color to highlight the text with.
     * 
     * Settable; Typst type: none|color|gradient|tiling
     */
    @all:Settable val fill: Option<TPaint>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/highlight/](https://typst.app/docs/reference/text/highlight/)
     * 
     * The highlight's border color. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-stroke) for more details.
     * 
     * Settable; Typst type: none|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Sides<Option<TStroke>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/highlight/](https://typst.app/docs/reference/text/highlight/)
     * 
     * The top end of the background rectangle.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"ascender"` | The font's ascender, which typically exceeds the height of all glyphs. |
     * | `"cap-height"` | The approximate height of uppercase letters. |
     * | `"x-height"` | The approximate height of non-ascending lowercase letters. |
     * | `"baseline"` | The baseline on which the letters rest. |
     * | `"bounds"` | The top edge of the glyph's bounding box. |
     * 
     * Settable; Typst type: length|str
     */
    @all:Settable val topEdge: TopEdge? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/highlight/](https://typst.app/docs/reference/text/highlight/)
     * 
     * The bottom end of the background rectangle.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"baseline"` | The baseline on which the letters rest. |
     * | `"descender"` | The font's descender, which typically exceeds the depth of all glyphs. |
     * | `"bounds"` | The bottom edge of the glyph's bounding box. |
     * 
     * Settable; Typst type: length|str
     */
    @all:Settable val bottomEdge: BottomEdge? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/highlight/](https://typst.app/docs/reference/text/highlight/)
     * 
     * The amount by which to extend the background to the sides beyond (or within if negative) the content.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val extent: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/highlight/](https://typst.app/docs/reference/text/highlight/)
     * 
     * How much to round the highlight's corners. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-radius) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val radius: Corners<TRelative>? = null,
    override val label: TLabel? = null
) : TSelectableContent<THighlight>() {
    override fun elem(): TLocatableElement<THighlight> = ELEM

    companion object {
        val ELEM = TLocatableElement<THighlight>("highlight")
    }
}


@SerialName("set-highlight")
data class TSetHighlight(
    override val internals: SetRuleInternals? = null,
    val fill: Option<TPaint>? = null,
    val stroke: Sides<Option<TStroke>>? = null,
    val topEdge: TopEdge? = null,
    val bottomEdge: BottomEdge? = null,
    val extent: TLength? = null,
    val radius: Corners<TRelative>? = null
) : TSetRule()
