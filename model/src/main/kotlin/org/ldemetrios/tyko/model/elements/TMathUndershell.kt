package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/underover/#functions-undershell
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-undershell](https://typst.app/docs/reference/math/underover/#functions-undershell)
 * 
 * A horizontal tortoise shell bracket under content, with an optional annotation below.
 * 
 * 
 */
@SerialName("math.undershell")
data class TMathUndershell(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-undershell](https://typst.app/docs/reference/math/underover/#functions-undershell)
     * 
     * The content above the tortoise shell bracket.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-undershell](https://typst.app/docs/reference/math/underover/#functions-undershell)
     * 
     * The optional content below the tortoise shell bracket.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Positional @all:Settable val annotation: Option<TContent>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.undershell")
    }
}


@SerialName("set-math.undershell")
data class TSetMathUndershell(
    override val internals: SetRuleInternals? = null,
    val annotation: Option<TContent>? = null
) : TSetRule()
