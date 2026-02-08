package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/visualize/curve/#definitions-line
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Adds a straight line from the current point to a following one.
 * 
 * 
 */
@SerialName("curve.line")
data class TCurveLine(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The point at which the line shall end.
     * 
     * Required, positional; Typst type: array
     */
    @all:Positional val end: Point<TRelative>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether the coordinates are relative to the previous point.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val relative: TBool? = null,
    override val label: TLabel? = null
) : TCurveComponent(label) {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("curve.line")
    }
}


@SerialName("set-curve.line")
data class TSetCurveLine(
    override val internals: SetRuleInternals? = null,
    val relative: TBool? = null
) : TSetRule()
