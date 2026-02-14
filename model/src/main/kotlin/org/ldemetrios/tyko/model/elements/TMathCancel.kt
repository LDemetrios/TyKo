package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/cancel/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/cancel/](https://typst.app/docs/reference/math/cancel/)
 * 
 * Displays a diagonal line over a part of an equation.
 * 
 * This is commonly used to show the elimination of a term.
 * 
 * **_Example_**
 * 
 * ```typ
 * Here, we can simplify:
 * $ (a dot b dot cancel(x)) /
 *     cancel(x) $
 * ```
 * <img src="https://typst.app/assets/docs/fVEZvXjKTk2s3WO88t3K8AAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("math.cancel")
data class TMathCancel(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/cancel/](https://typst.app/docs/reference/math/cancel/)
     * 
     * The content over which the line should be placed.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/cancel/](https://typst.app/docs/reference/math/cancel/)
     * 
     * The length of the line, relative to the length of the diagonal spanning the whole element being "cancelled". A value of `100%` would then have the line span precisely the element's diagonal.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val length: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/cancel/](https://typst.app/docs/reference/math/cancel/)
     * 
     * Whether the cancel line should be inverted (flipped along the y-axis). For the default angle setting, inverted means the cancel line points to the top left instead of top right.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val inverted: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/cancel/](https://typst.app/docs/reference/math/cancel/)
     * 
     * Whether two opposing cancel lines should be drawn, forming a cross over the element. Overrides `inverted`.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val cross: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/cancel/](https://typst.app/docs/reference/math/cancel/)
     * 
     * How much to rotate the cancel line.
     * 
     * - If given an angle, the line is rotated by that angle clockwise with respect to the y-axis.
     * - If `auto`, the line assumes the default angle; that is, along the rising diagonal of the content box.
     * - If given a function `angle => angle`, the line is rotated, with respect to the y-axis, by the angle returned by that function. The function receives the default angle as its input.
     * 
     * Settable; Typst type: auto|angle|function
     */
    @all:Settable val angle: Smart<Computable<TAngle>>? = 1.0.t.deg,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/cancel/](https://typst.app/docs/reference/math/cancel/)
     * 
     * How to [stroke](https://typst.app/docs/reference/visualize/stroke/) the cancel line.
     * 
     * Settable; Typst type: length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: TStroke? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.cancel")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TMathCancel]
 */
@SerialName("set-math.cancel")
data class TSetMathCancel(
    override val internals: SetRuleInternals? = null,
    val length: TRelative? = null,
    val inverted: TBool? = null,
    val cross: TBool? = null,
    val angle: Smart<Computable<TAngle>>? = null,
    val stroke: TStroke? = null
) : TSetRule()
