package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/underover/#functions-underparen
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-underparen](https://typst.app/docs/reference/math/underover/#functions-underparen)
 * 
 * A horizontal parenthesis under content, with an optional annotation below.
 * 
 * 
 */
@SerialName("math.underparen")
data class TMathUnderparen(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-underparen](https://typst.app/docs/reference/math/underover/#functions-underparen)
     * 
     * The content above the parenthesis.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-underparen](https://typst.app/docs/reference/math/underover/#functions-underparen)
     * 
     * The optional content below the parenthesis.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Positional @all:Settable val annotation: Option<TContent>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.underparen")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TMathUnderparen]
 */
@SerialName("set-math.underparen")
data class TSetMathUnderparen(
    override val internals: SetRuleInternals? = null,
    val annotation: Option<TContent>? = null
) : TSetRule()
