package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/lr/#functions-lr
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Scales delimiters.
 * 
 * While matched delimiters scale by default, this can be used to scale unmatched delimiters and to control the delimiter scaling more precisely.
 */
@SerialName("math.lr")
data class TMathLr(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The delimited content, including the delimiters.
     * 
     * Required, positional; Typst type: content
     */
    @all:Body @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The size of the brackets, relative to the height of the wrapped content.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val size: TRelative? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.lr")
    }
}

@SerialName("set-math.lr")
data class TSetMathLimits(
    override val internals: SetRuleInternals? = null,
    val size: TRelative? = null,
) : TSetRule()
