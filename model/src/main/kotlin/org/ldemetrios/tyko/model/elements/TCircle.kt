package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.Spacing
import org.ldemetrios.tyko.model.TPaint

//!https://typst.app/docs/reference/visualize/circle/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/circle/](https://typst.app/docs/reference/visualize/circle/)
 * 
 * A circle with optional content.
 * 
 * **_Example_**
 * 
 * ```typ
 * // Without content.
 * #circle(radius: 25pt)
 * 
 * // With content.
 * #circle[
 *   #set align(center + horizon)
 *   Automatically \
 *   sized to fit.
 * ]
 * ```
 * <img src="https://typst.app/assets/docs/H1niwFeoKUTVgzuqcmZ_VgAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("circle")
data class TCircle(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/circle/](https://typst.app/docs/reference/visualize/circle/)
     * 
     * The content to place into the circle. The circle expands to fit this content, keeping the 1-1 aspect ratio.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Settable @all:Positional val body: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/circle/](https://typst.app/docs/reference/visualize/circle/)
     * 
     * The circle's radius. This is mutually exclusive with `width` and `height`.
     * 
     * Typst type: length
     */
    val radius: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/circle/](https://typst.app/docs/reference/visualize/circle/)
     * 
     * The circle's width. This is mutually exclusive with `radius` and `height`.
     * 
     * In contrast to `radius`, this can be relative to the parent container's width.
     * 
     * Settable; Typst type: auto|relative
     */
    @all:Settable val width: Smart<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/circle/](https://typst.app/docs/reference/visualize/circle/)
     * 
     * The circle's height. This is mutually exclusive with `radius` and `width`.
     * 
     * In contrast to `radius`, this can be relative to the parent container's height.
     * 
     * Settable; Typst type: auto|relative|fraction
     */
    @all:Settable val height: Smart<Spacing>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/circle/](https://typst.app/docs/reference/visualize/circle/)
     * 
     * How to fill the circle. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-fill) for more details.
     * 
     * Settable; Typst type: none|color|gradient|tiling
     */
    @all:Settable val fill: Option<TPaint>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/circle/](https://typst.app/docs/reference/visualize/circle/)
     * 
     * How to stroke the circle. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-stroke) for more details.
     * 
     * Settable; Typst type: none|auto|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Sides<Smart<Option<TStroke>>>? = null, // TODO is it really Sides?
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/circle/](https://typst.app/docs/reference/visualize/circle/)
     * 
     * How much to pad the circle's content. See the [box's documentation](https://typst.app/docs/reference/layout/box/#parameters-inset) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val inset: Sides<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/circle/](https://typst.app/docs/reference/visualize/circle/)
     * 
     * How much to expand the circle's size without affecting the layout. See the [box's documentation](https://typst.app/docs/reference/layout/box/#parameters-outset) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val outset: Sides<TRelative>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("circle")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TCircle]
 */
@SerialName("set-circle")
data class TSetCircle(
    override val internals: SetRuleInternals? = null,
    @all:Positional val body: Option<TContent>? = null,
    val width: Smart<TRelative>? = null,
    val height: Smart<Spacing>? = null,
    val fill: Option<TPaint>? = null,
    val stroke: Sides<Smart<Option<TStroke>>>? = null,
    val inset: Sides<TRelative>? = null,
    val outset: Sides<TRelative>? = null
) : TSetRule()
