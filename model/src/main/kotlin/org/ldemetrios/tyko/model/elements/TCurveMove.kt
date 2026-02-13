package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/visualize/curve/#definitions-move
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-move](https://typst.app/docs/reference/visualize/curve/#definitions-move)
 * 
 * Starts a new curve component.
 * 
 * If no `curve.move` element is passed, the curve will start at `(0pt, 0pt)`.
 * 
 * 
 */
@SerialName("curve.move")
data class TCurveMove(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-move](https://typst.app/docs/reference/visualize/curve/#definitions-move)
     * 
     * The starting point for the new component.
     * 
     * Required, positional; Typst type: array
     */
    @all:Positional val start: Point<TRelative>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-move](https://typst.app/docs/reference/visualize/curve/#definitions-move)
     * 
     * Whether the coordinates are relative to the previous point.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val relative: TBool? = null,
    override val label: TLabel? = null
) : TCurveComponent(label) {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("curve.move")
    }
}


@SerialName("set-curve.move")
data class TSetCurveMove(
    override val internals: SetRuleInternals? = null,
    val relative: TBool? = null
) : TSetRule()
