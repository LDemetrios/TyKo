package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/terms/#definitions-item
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/terms/#definitions-item](https://typst.app/docs/reference/model/terms/#definitions-item)
 * 
 * A term list item.
 */
@SerialName("terms.item")
data class TTermsItem(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/terms/#definitions-item](https://typst.app/docs/reference/model/terms/#definitions-item)
     * 
     * The term described by the list item.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val term: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/terms/#definitions-item](https://typst.app/docs/reference/model/terms/#definitions-item)
     * 
     * The description of the term.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val description: TContent,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("terms.item")
    }
}
