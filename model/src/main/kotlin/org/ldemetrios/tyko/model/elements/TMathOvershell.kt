package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/underover/#functions-overshell
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-overshell](https://typst.app/docs/reference/math/underover/#functions-overshell)
 * 
 * A horizontal tortoise shell bracket over content, with an optional annotation above.
 * 
 * 
 */
@SerialName("math.overshell")
data class TMathOvershell(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-overshell](https://typst.app/docs/reference/math/underover/#functions-overshell)
     * 
     * The content below the tortoise shell bracket.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-overshell](https://typst.app/docs/reference/math/underover/#functions-overshell)
     * 
     * The optional content above the tortoise shell bracket.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Positional @all:Settable val annotation: Option<TContent>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.overshell")
    }
}


@SerialName("set-math.overshell")
data class TSetMathOvershell(
    override val internals: SetRuleInternals? = null,
    val annotation: Option<TContent>? = null
) : TSetRule()
