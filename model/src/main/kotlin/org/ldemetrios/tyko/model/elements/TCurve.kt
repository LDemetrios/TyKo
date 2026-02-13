package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


sealed class TCurveComponent(override val label: TLabel?): TContent()

//!https://typst.app/docs/reference/visualize/curve/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/curve/](https://typst.app/docs/reference/visualize/curve/)
 * 
 * A curve consisting of movements, lines, and Bézier segments.
 * 
 * At any point in time, there is a conceptual pen or cursor.
 * 
 * - Move elements move the cursor without drawing.
 * - Line/Quadratic/Cubic elements draw a segment from the cursor to a new position, potentially with control point for a Bézier curve.
 * - Close elements draw a straight or smooth line back to the start of the curve or the latest preceding move segment.
 * 
 * For layout purposes, the bounding box of the curve is a tight rectangle containing all segments as well as the point `(0pt, 0pt)`.
 * 
 * Positions may be specified absolutely (i.e. relatively to `(0pt, 0pt)`), or relative to the current pen/cursor position, that is, the position where the previous segment ended.
 * 
 * Bézier curve control points can be skipped by passing `none` or automatically mirrored from the preceding segment by passing `auto`.
 * 
 * **_Example_**
 * 
 * ```typ
 * #curve(
 *   fill: blue.lighten(80%),
 *   stroke: blue,
 *   curve.move((0pt, 50pt)),
 *   curve.line((100pt, 50pt)),
 *   curve.cubic(none, (90pt, 0pt), (50pt, 0pt)),
 *   curve.close(),
 * )
 * ```
 * <img src="https://typst.app/assets/docs/9uihNAu_dEPJt6RrdXvspAAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("curve")
data class TCurve(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/](https://typst.app/docs/reference/visualize/curve/)
     * 
     * The components of the curve, in the form of moves, line and Bézier segment, and closes.
     * 
     * Required, positional, variadic; Typst type: content
     */
    @all:Variadic @all:Positional val components: TArray<TCurveComponent>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/](https://typst.app/docs/reference/visualize/curve/)
     * 
     * How to fill the curve.
     * 
     * When setting a fill, the default stroke disappears. To create a curve with both fill and stroke, you have to configure both.
     * 
     * Settable; Typst type: none|color|gradient|tiling
     */
    @all:Settable val fill: Option<TPaint>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/](https://typst.app/docs/reference/visualize/curve/)
     * 
     * The drawing rule used to fill the curve.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"non-zero"` | Specifies that "inside" is computed by a non-zero sum of signed edge crossings. |
     * | `"even-odd"` | Specifies that "inside" is computed by an odd number of edge crossings. |
     * 
     * Settable; Typst type: str
     */
    @all:SerialName("fill-rule") @all:Settable val fillRule: TFillRule? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/](https://typst.app/docs/reference/visualize/curve/)
     * 
     * How to [stroke](https://typst.app/docs/reference/visualize/stroke/) the curve.
     * 
     * Can be set to `none` to disable the stroke or to `auto` for a stroke of `1pt` black if and only if no fill is given.
     * 
     * Settable; Typst type: none|auto|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Smart<Option<TStroke>>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("curve")
    }
}


@SerialName("set-curve")
data class TSetCurve(
    override val internals: SetRuleInternals? = null,
    val fill: Option<TPaint>? = null,
    val fillRule: TStr? = null,
    val stroke: Smart<Option<TStroke>>? = null
) : TSetRule()
