package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.Spacing
import org.ldemetrios.tyko.model.TPaint

//!https://typst.app/docs/reference/layout/box/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/box/](https://typst.app/docs/reference/layout/box/)
 * 
 * An inline-level container that sizes content.
 * 
 * All elements except inline math, text, and boxes are block-level and cannot occur inside of a [paragraph](https://typst.app/docs/reference/model/par/). The box function can be used to integrate such elements into a paragraph. Boxes take the size of their contents by default but can also be sized explicitly.
 * 
 * **_Example_**
 * 
 * ```typ
 * Refer to the docs
 * #box(
 *   height: 9pt,
 *   image("docs.svg")
 * )
 * for more information.
 * ```
 * <img src="https://typst.app/assets/docs/eB9NAzu2xk-O1miffozwKQAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("box")
data class TBox(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/box/](https://typst.app/docs/reference/layout/box/)
     * 
     * The contents of the box.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Settable @all:Positional val body: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/box/](https://typst.app/docs/reference/layout/box/)
     * 
     * The width of the box.
     * 
     * Boxes can have [fractional](https://typst.app/docs/reference/layout/fraction/) widths, as the example below demonstrates.
     * 
     * *Note:* Currently, only boxes and only their widths might be fractionally sized within paragraphs. Support for fractionally sized images, shapes, and more might be added in the future.
     * 
     * Settable; Typst type: auto|relative|fraction
     */
    @all:Settable val width: Smart<Spacing>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/box/](https://typst.app/docs/reference/layout/box/)
     * 
     * The height of the box.
     * 
     * Settable; Typst type: auto|relative
     */
    @all:Settable val height: Smart<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/box/](https://typst.app/docs/reference/layout/box/)
     * 
     * An amount to shift the box's baseline by.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val baseline: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/box/](https://typst.app/docs/reference/layout/box/)
     * 
     * The box's background color. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-fill) for more details.
     * 
     * Settable; Typst type: none|color|gradient|tiling
     */
    @all:Settable val fill: Option<TPaint>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/box/](https://typst.app/docs/reference/layout/box/)
     * 
     * The box's border color. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-stroke) for more details.
     * 
     * Settable; Typst type: none|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Sides<Option<TStroke>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/box/](https://typst.app/docs/reference/layout/box/)
     * 
     * How much to round the box's corners. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-radius) for more details.
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val radius: Corners<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/box/](https://typst.app/docs/reference/layout/box/)
     * 
     * How much to pad the box's content.
     * 
     * This can be a single length for all sides or a dictionary of lengths for individual sides. When passing a dictionary, it can contain the following keys in order of precedence: `top`, `right`, `bottom`, `left` (controlling the respective cell sides), `x`, `y` (controlling vertical and horizontal insets), and `rest` (covers all insets not styled by other dictionary entries). All keys are optional; omitted keys will use their previously set value, or the default value if never set.
     * 
     * [Relative lengths](https://typst.app/docs/reference/layout/relative/) for this parameter are relative to the box size excluding [outset](https://typst.app/docs/reference/layout/box/#parameters-outset). Note that relative insets and outsets are different from relative [widths](https://typst.app/docs/reference/layout/box/#parameters-width) and [heights](https://typst.app/docs/reference/layout/box/#parameters-height), which are relative to the container.
     * 
     * *Note:* When the box contains text, its exact size depends on the current [text edges](https://typst.app/docs/reference/text/text/#parameters-top-edge).
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val inset: Sides<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/box/](https://typst.app/docs/reference/layout/box/)
     * 
     * How much to expand the box's size without affecting the layout.
     * 
     * This can be a single length for all sides or a dictionary of lengths for individual sides. [Relative lengths](https://typst.app/docs/reference/layout/relative/) for this parameter are relative to the box size excluding outset. See the documentation for [inset](https://typst.app/docs/reference/layout/box/#parameters-inset) above for further details.
     * 
     * This is useful to prevent padding from affecting line layout. For a generalized version of the example below, see the documentation for the [raw text's block parameter](https://typst.app/docs/reference/text/raw/#parameters-block).
     * 
     * Settable; Typst type: relative|dictionary
     */
    @all:Settable val outset: Sides<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/box/](https://typst.app/docs/reference/layout/box/)
     * 
     * Whether to clip the content inside the box.
     * 
     * Clipping is useful when the box's content is larger than the box itself, as any content that exceeds the box's bounds will be hidden.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val clip: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("box")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TBox]
 */
@SerialName("set-box")
data class TSetBox(
    override val internals: SetRuleInternals? = null,
    @all:Positional val body: Option<TContent>? = null,
    val width: Smart<Spacing>? = null,
    val height: Smart<TRelative>? = null,
    val baseline: TRelative? = null,
    val fill: Option<TPaint>? = null,
    val stroke: Sides<Option<TStroke>>? = null,
    val radius: Corners<TRelative>? = null,
    val inset: Sides<TRelative>? = null,
    val outset: Sides<TRelative>? = null,
    val clip: TBool? = null
) : TSetRule()
