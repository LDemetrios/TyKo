package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/place/#definitions-flush
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/place/#definitions-flush](https://typst.app/docs/reference/layout/place/#definitions-flush)
 * 
 * Asks the layout algorithm to place pending floating elements before continuing with the content.
 * 
 * This is useful for preventing floating figures from spilling into the next section.
 * 
 * 
 */
@SerialName("place.flush")
data class TPlaceFlush(
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("place.flush")
    }
}
