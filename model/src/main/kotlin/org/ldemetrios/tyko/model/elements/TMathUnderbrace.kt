package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/underover/#functions-underbrace
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-underbrace](https://typst.app/docs/reference/math/underover/#functions-underbrace)
 * 
 * A horizontal brace under content, with an optional annotation below.
 * 
 * 
 */
@SerialName("math.underbrace")
data class TMathUnderbrace(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-underbrace](https://typst.app/docs/reference/math/underover/#functions-underbrace)
     * 
     * The content above the brace.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-underbrace](https://typst.app/docs/reference/math/underover/#functions-underbrace)
     * 
     * The optional content below the brace.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Positional @all:Settable val annotation: Option<TContent>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.underbrace")
    }
}


@SerialName("set-math.underbrace")
data class TSetMathUnderbrace(
    override val internals: SetRuleInternals? = null,
    val annotation: Option<TContent>? = null
) : TSetRule()
