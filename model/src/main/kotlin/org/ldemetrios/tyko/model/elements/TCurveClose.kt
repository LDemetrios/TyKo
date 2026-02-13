package org.ldemetrios.tyko.model

//!https://typst.app/docs/reference/visualize/curve/#definitions-close
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-close](https://typst.app/docs/reference/visualize/curve/#definitions-close)
 * 
 * Closes the curve by adding a segment from the last point to the start of the curve (or the last preceding `curve.move` point).
 * 
 * 
 */
@SerialName("curve.close")
data class TCurveClose(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-close](https://typst.app/docs/reference/visualize/curve/#definitions-close)
     * 
     * How to close the curve.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"smooth"` | Closes the curve with a smooth segment that takes into account the control point opposite the start point. |
     * | `"straight"` | Closes the curve with a straight line. |
     * 
     * Settable; Typst type: str
     */
    @all:Settable val mode: TLineCloseMode? = null,
    override val label: TLabel? = null
) : TCurveComponent(label) {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("curve.close")
    }
}


@SerialName("set-curve.close")
data class TSetCurveClose(
    override val internals: SetRuleInternals? = null,
    val mode: TLineCloseMode? = null
) : TSetRule()
