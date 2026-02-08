package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/underover/#functions-overparen
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A horizontal parenthesis over content, with an optional annotation above.
 * 
 * 
 */
@SerialName("math.overparen")
data class TMathOverparen(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content below the parenthesis.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The optional content above the parenthesis.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Positional @all:Settable val annotation: Option<TContent>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.overparen")
    }
}


@SerialName("set-math.overparen")
data class TSetMathOverparen(
    override val internals: SetRuleInternals? = null,
    val annotation: Option<TContent>? = null
) : TSetRule()
