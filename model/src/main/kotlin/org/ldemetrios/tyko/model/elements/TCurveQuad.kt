package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


//!https://typst.app/docs/reference/visualize/curve/#definitions-quad
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-quad](https://typst.app/docs/reference/visualize/curve/#definitions-quad)
 * 
 * Adds a quadratic Bézier curve segment from the last point to `end`, using `control` as the control point.
 * 
 * 
 */
@SerialName("curve.quad")
data class TCurveQuad(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-quad](https://typst.app/docs/reference/visualize/curve/#definitions-quad)
     * 
     * The control point of the quadratic Bézier curve.
     * 
     * - If `auto` and this segment follows another quadratic Bézier curve, the previous control point will be mirrored.
     * - If `none`, the control point defaults to `end`, and the curve will be a straight line.
     * 
     * Required, positional; Typst type: none|auto|array
     */
    @all:Positional val control: Smart<TCurveControl<TRelative>>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-quad](https://typst.app/docs/reference/visualize/curve/#definitions-quad)
     * 
     * The point at which the segment shall end.
     * 
     * Required, positional; Typst type: array
     */
    @all:Positional val end: Point<TRelative>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-quad](https://typst.app/docs/reference/visualize/curve/#definitions-quad)
     * 
     * Whether the `control` and `end` coordinates are relative to the previous point.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val relative: TBool? = null,
    override val label: TLabel? = null
) : TCurveComponent(label) {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("curve.quad")
    }
}


@SerialName("set-curve.quad")
data class TSetCurveQuad(
    override val internals: SetRuleInternals? = null,
    val relative: TBool? = null
) : TSetRule()
