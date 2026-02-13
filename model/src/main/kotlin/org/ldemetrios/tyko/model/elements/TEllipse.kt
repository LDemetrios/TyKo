package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/visualize/ellipse/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/ellipse/](https://typst.app/docs/reference/visualize/ellipse/)
 * 
 * An ellipse with optional content.
 * 
 * **_Example_**
 * 
 * ```typ
 * // Without content.
 * #ellipse(width: 35%, height: 30pt)
 * 
 * // With content.
 * #ellipse[
 *   #set align(center)
 *   Automatically sized \
 *   to fit the content.
 * ]
 * ```
 * <img src="https://typst.app/assets/docs/u35LFJMn0LDLxUBqOdjmvgAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("ellipse")
data class TEllipse(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/ellipse/](https://typst.app/docs/reference/visualize/ellipse/)
     * 
     * The content to place into the ellipse.
     * 
     * When this is omitted, the ellipse takes on a default size of at most `45pt` by `30pt`.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Settable @all:Positional val body: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/ellipse/](https://typst.app/docs/reference/visualize/ellipse/)
     * 
     * The ellipse's width, relative to its parent container.
     * 
     * Settable; Typst type: auto|relative
     */
    @all:Settable val width: Smart<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/ellipse/](https://typst.app/docs/reference/visualize/ellipse/)
     * 
     * The ellipse's height, relative to its parent container.
     * 
     * Settable; Typst type: auto|relative|fraction
     */
    @all:Settable val height: Smart<Spacing>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/ellipse/](https://typst.app/docs/reference/visualize/ellipse/)
     * 
     * How to fill the ellipse. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-fill) for more details.
     * 
     * Settable; Typst type: none|color|gradient|tiling
     */
    @all:Settable val fill: Option<TPaint>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/ellipse/](https://typst.app/docs/reference/visualize/ellipse/)
     * 
     * How to stroke the ellipse. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-stroke) for more details.
     * 
     * Settable; Typst type: none|auto|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Sides<Smart<Option<TStroke>>>? = null, // TODO really Sides>
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/ellipse/](https://typst.app/docs/reference/visualize/ellipse/)
     * 
     * How much to pad the ellipse's content. See the [box's documentation](https://typst.app/docs/reference/layout/box/#parameters-inset) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val inset: Sides<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/ellipse/](https://typst.app/docs/reference/visualize/ellipse/)
     * 
     * How much to expand the ellipse's size without affecting the layout. See the [box's documentation](https://typst.app/docs/reference/layout/box/#parameters-outset) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val outset: Sides<TRelative>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("ellipse")
    }
}


@SerialName("set-ellipse")
data class TSetEllipse(
    override val internals: SetRuleInternals? = null,
    @all:Positional val body: Option<TContent>? = null,
    val width: Option<TRelative>? = null,
    val height: Smart<Spacing>? = null,
    val fill: Option<TPaint>? = null,
    val stroke: Sides<Smart<Option<TStroke>>>? = null,
    val inset: Sides<TRelative>? = null,
    val outset: Sides<TRelative>? = null
) : TSetRule()
