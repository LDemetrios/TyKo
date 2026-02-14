package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.Spacing
import org.ldemetrios.tyko.model.TPaint


//!https://typst.app/docs/reference/visualize/square/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/square/](https://typst.app/docs/reference/visualize/square/)
 * 
 * A square with optional content.
 * 
 * **_Example_**
 * 
 * ```typ
 * // Without content.
 * #square(size: 40pt)
 * 
 * // With content.
 * #square[
 *   Automatically \
 *   sized to fit.
 * ]
 * ```
 * <img src="https://typst.app/assets/docs/DjWoCmaGrn_miIIjOqjv7gAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("square")
data class TSquare(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/square/](https://typst.app/docs/reference/visualize/square/)
     * 
     * The content to place into the square. The square expands to fit this content, keeping the 1-1 aspect ratio.
     * 
     * When this is omitted, the square takes on a default size of at most `30pt`.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Settable @all:Positional val body: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/square/](https://typst.app/docs/reference/visualize/square/)
     * 
     * The square's side length. This is mutually exclusive with `width` and `height`.
     * 
     * Typst type: auto|length
     */
    val size: Smart<TLength>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/square/](https://typst.app/docs/reference/visualize/square/)
     * 
     * The square's width. This is mutually exclusive with `size` and `height`.
     * 
     * In contrast to `size`, this can be relative to the parent container's width.
     * 
     * Settable; Typst type: auto|relative
     */
    @all:Settable val width: Smart<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/square/](https://typst.app/docs/reference/visualize/square/)
     * 
     * The square's height. This is mutually exclusive with `size` and `width`.
     * 
     * In contrast to `size`, this can be relative to the parent container's height.
     * 
     * Settable; Typst type: auto|relative|fraction
     */
    @all:Settable val height: Smart<Spacing>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/square/](https://typst.app/docs/reference/visualize/square/)
     * 
     * How to fill the square. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-fill) for more details.
     * 
     * Settable; Typst type: none|color|gradient|tiling
     */
    @all:Settable val fill: Option<TPaint>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/square/](https://typst.app/docs/reference/visualize/square/)
     * 
     * How to stroke the square. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-stroke) for more details.
     * 
     * Settable; Typst type: none|auto|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Sides<Smart<Option<TStroke>>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/square/](https://typst.app/docs/reference/visualize/square/)
     * 
     * How much to round the square's corners. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-radius) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val radius: Corners<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/square/](https://typst.app/docs/reference/visualize/square/)
     * 
     * How much to pad the square's content. See the [box's documentation](https://typst.app/docs/reference/layout/box/#parameters-inset) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val inset: Sides<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/square/](https://typst.app/docs/reference/visualize/square/)
     * 
     * How much to expand the square's size without affecting the layout. See the [box's documentation](https://typst.app/docs/reference/layout/box/#parameters-outset) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val outset: Sides<TRelative>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("square")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TSquare]
 */
@SerialName("set-square")
data class TSetSquare(
    override val internals: SetRuleInternals? = null,
    @all:Positional val body: Option<TContent>? = null,
    val width: Smart<TRelative>? = null,
    val height: Smart<Spacing>? = null,
    val fill: Option<TPaint>? = null,
    val stroke: Sides<Smart<Option<TStroke>>>? = null,
    val radius: Corners<TRelative>? = null,
    val inset: Sides<TRelative>? = null,
    val outset: Sides<TRelative>? = null
) : TSetRule()
