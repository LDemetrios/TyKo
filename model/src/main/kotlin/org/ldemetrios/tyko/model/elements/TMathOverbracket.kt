package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/underover/#functions-overbracket
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-overbracket](https://typst.app/docs/reference/math/underover/#functions-overbracket)
 * 
 * A horizontal bracket over content, with an optional annotation above.
 * 
 * 
 */
@SerialName("math.overbracket")
data class TMathOverbracket(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-overbracket](https://typst.app/docs/reference/math/underover/#functions-overbracket)
     * 
     * The content below the bracket.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-overbracket](https://typst.app/docs/reference/math/underover/#functions-overbracket)
     * 
     * The optional content above the bracket.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Positional @all:Settable val annotation: Option<TContent>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.overbracket")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TMathOverbracket]
 */
@SerialName("set-math.overbracket")
data class TSetMathOverbracket(
    override val internals: SetRuleInternals? = null,
    val annotation: Option<TContent>? = null
) : TSetRule()
