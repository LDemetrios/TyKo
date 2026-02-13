package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/text/overline/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/text/overline/](https://typst.app/docs/reference/text/overline/)
 * 
 * Adds a line over text.
 * 
 * **_Example_**
 * 
 * ```typ
 * #overline[A line over text.]
 * ```
 * <img src="https://typst.app/assets/docs/BQmJqK4pMIkZOu3QEFxsZAAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("overline")
data class TOverline(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/overline/](https://typst.app/docs/reference/text/overline/)
     * 
     * The content to add a line over.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/overline/](https://typst.app/docs/reference/text/overline/)
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
     * Generated based on: [https://typst.app/docs/reference/text/overline/](https://typst.app/docs/reference/text/overline/)
     * 
     * The position of the line relative to the baseline. Read from the font tables if `auto`.
     * 
     * Settable; Typst type: auto|length
     */
    @all:Settable val offset: Smart<TLength>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/overline/](https://typst.app/docs/reference/text/overline/)
     * 
     * The amount by which to extend the line beyond (or within if negative) the content.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val extent: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/overline/](https://typst.app/docs/reference/text/overline/)
     * 
     * Whether the line skips sections in which it would collide with the glyphs.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val evade: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/overline/](https://typst.app/docs/reference/text/overline/)
     * 
     * Whether the line is placed behind the content it overlines.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val background: TBool? = null,
    override val label: TLabel? = null
) : TSelectableContent<TOverline>() {
    override fun elem(): TLocatableElement<TOverline> = ELEM

    companion object {
        val ELEM = TLocatableElement<TOverline>("overline")
    }
}


@SerialName("set-overline")
data class TSetOverline(
    override val internals: SetRuleInternals? = null,
    val stroke: Smart<TStroke>? = null,
    val offset: Smart<TLength>? = null,
    val extent: TLength? = null,
    val evade: TBool? = null,
    val background: TBool? = null
) : TSetRule()
