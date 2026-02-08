package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


//!https://typst.app/docs/reference/math/attach/#functions-limits
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Forces a base to display attachments as limits.
 * 
 * 
 */
@SerialName("math.limits")
data class TMathLimits(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The base to attach the limits to.
     * 
     * Required, positional; Typst type: content
     */
    @all:Body @all:Positional val body: TContent? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether to also force limits in inline equations.
     * 
     * When applying limits globally (e.g., through a show rule), it is typically a good idea to disable this.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val inline: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.limits")
    }
}


@SerialName("set-math.limits")
data class TSetMathLr(
    override val internals: SetRuleInternals? = null,
    val inline: TBool? = null,
) : TSetRule()
