package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/list/#definitions-item
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/list/#definitions-item](https://typst.app/docs/reference/model/list/#definitions-item)
 * 
 * A bullet list item.
 */
@SerialName("list.item")
data class TListItem(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/list/#definitions-item](https://typst.app/docs/reference/model/list/#definitions-item)
     * 
     * The item's body.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("list.item")
    }
}
