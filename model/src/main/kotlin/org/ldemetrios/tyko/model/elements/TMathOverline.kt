package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/underover/#functions-overline
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-overline](https://typst.app/docs/reference/math/underover/#functions-overline)
 * 
 * A horizontal line over content.
 * 
 * 
 */
@SerialName("math.overline")
data class TMathOverline(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/underover/#functions-overline](https://typst.app/docs/reference/math/underover/#functions-overline)
     * 
     * The content below the line.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.overline")
    }
}
