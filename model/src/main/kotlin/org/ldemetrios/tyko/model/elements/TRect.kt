package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/visualize/rect/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A rectangle with optional content.
 * 
 * **_Example_**
 * 
 * ```typ
 * // Without content.
 * #rect(width: 35%, height: 30pt)
 * 
 * // With content.
 * #rect[
 *   Automatically sized \
 *   to fit the content.
 * ]
 * ```
 * <img src="https://typst.app/assets/docs/uMLkrKs8AmOe9L-qU4CYKgAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("rect")
data class TRect(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content to place into the rectangle.
     * 
     * When this is omitted, the rectangle takes on a default size of at most `45pt` by `30pt`.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Settable @all:Positional val body: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The rectangle's width, relative to its parent container.
     * 
     * Settable; Typst type: auto|relative
     */
    @all:Settable val width: Smart<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The rectangle's height, relative to its parent container.
     * 
     * Settable; Typst type: auto|relative|fraction
     */
    @all:Settable val height: Smart<Spacing>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * How to fill the rectangle.
     * 
     * When setting a fill, the default stroke disappears. To create a rectangle with both fill and stroke, you have to configure both.
     * 
     * Settable; Typst type: none|color|gradient|tiling
     */
    @all:Settable val fill: Option<TPaint>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * How to stroke the rectangle. This can be:
     * 
     * - `none` to disable stroking
     * - `auto` for a stroke of `1pt + black` if and only if no fill is given.
     * - Any kind of [stroke](https://typst.app/docs/reference/visualize/stroke/)
     * - A dictionary describing the stroke for each side individually. The dictionary can contain the following keys in order of precedence:All keys are optional; omitted keys will use their previously set value, or the default stroke if never set.
     *   - `top`: The top stroke.
     *   - `right`: The right stroke.
     *   - `bottom`: The bottom stroke.
     *   - `left`: The left stroke.
     *   - `x`: The horizontal stroke.
     *   - `y`: The vertical stroke.
     *   - `rest`: The stroke on all sides except those for which the dictionary explicitly sets a size.
     * 
     * Settable; Typst type: none|auto|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Sides<Smart<Option<TStroke>>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * How much to round the rectangle's corners, relative to the minimum of the width and height divided by two. This can be:
     * 
     * - A relative length for a uniform corner radius.
     * - A dictionary: With a dictionary, the stroke for each side can be set individually. The dictionary can contain the following keys in order of precedence:
     *   - `top-left`: The top-left corner radius.
     *   - `top-right`: The top-right corner radius.
     *   - `bottom-right`: The bottom-right corner radius.
     *   - `bottom-left`: The bottom-left corner radius.
     *   - `left`: The top-left and bottom-left corner radii.
     *   - `top`: The top-left and top-right corner radii.
     *   - `right`: The top-right and bottom-right corner radii.
     *   - `bottom`: The bottom-left and bottom-right corner radii.
     *   - `rest`: The radii for all corners except those for which the dictionary explicitly sets a size.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val radius: Corners<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * How much to pad the rectangle's content. See the [box's documentation](https://typst.app/docs/reference/layout/box/#parameters-inset) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val inset: Sides<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * How much to expand the rectangle's size without affecting the layout. See the [box's documentation](https://typst.app/docs/reference/layout/box/#parameters-outset) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val outset: Sides<TRelative>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("rect")
    }
}


@SerialName("set-rect")
data class TSetRect(
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
