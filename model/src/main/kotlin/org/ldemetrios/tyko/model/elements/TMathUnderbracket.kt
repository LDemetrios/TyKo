package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/underover/#functions-underbracket
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-underbracket](https://typst.app/docs/reference/math/underover/#functions-underbracket)
 * 
 * A horizontal bracket under content, with an optional annotation below.
 * 
 * 
 */
@SerialName("math.underbracket")
data class TMathUnderbracket(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-underbracket](https://typst.app/docs/reference/math/underover/#functions-underbracket)
     * 
     * The content above the bracket.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-underbracket](https://typst.app/docs/reference/math/underover/#functions-underbracket)
     * 
     * The optional content below the bracket.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Positional @all:Settable val annotation: Option<TContent>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.underbracket")
    }
}


@SerialName("set-math.underbracket")
data class TSetMathUnderbracket(
    override val internals: SetRuleInternals? = null,
    val annotation: Option<TContent>? = null
) : TSetRule()
