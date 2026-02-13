package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/underover/#functions-underline
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-underline](https://typst.app/docs/reference/math/underover/#functions-underline)
 * 
 * A horizontal line under content.
 * 
 * 
 */
@SerialName("math.underline")
data class TMathUnderline(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-underline](https://typst.app/docs/reference/math/underover/#functions-underline)
     * 
     * The content above the line.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.underline")
    }
}
