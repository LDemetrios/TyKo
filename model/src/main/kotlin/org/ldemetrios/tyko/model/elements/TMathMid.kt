package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/lr/#functions-mid
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/lr/#functions-mid](https://typst.app/docs/reference/math/lr/#functions-mid)
 * 
 * Scales delimiters vertically to the nearest surrounding `lr()` group.
 * 
 * 
 */
@SerialName("math.mid")
data class TMathMid(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/lr/#functions-mid](https://typst.app/docs/reference/math/lr/#functions-mid)
     * 
     * The content to be scaled.
     * 
     * Required, positional; Typst type: content
     */
    @all:Body @all:Positional val body: TContent,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.mid")
    }
}
