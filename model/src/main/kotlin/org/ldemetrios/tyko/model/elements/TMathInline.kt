package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/sizes/#functions-inline
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/sizes/#functions-inline](https://typst.app/docs/reference/math/sizes/#functions-inline)
 * 
 * Forced inline (text) style in math.
 * 
 * This is the normal size for inline equations.
 * 
 * 
 */
@SerialName("math.inline")
data class TMathInline(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/sizes/#functions-inline](https://typst.app/docs/reference/math/sizes/#functions-inline)
     * 
     * The content to size.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/sizes/#functions-inline](https://typst.app/docs/reference/math/sizes/#functions-inline)
     * 
     * Whether to impose a height restriction for exponents, like regular sub- and superscripts do.
     * 
     * Typst type: bool
     */
    val cramped: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.inline")
    }
}
