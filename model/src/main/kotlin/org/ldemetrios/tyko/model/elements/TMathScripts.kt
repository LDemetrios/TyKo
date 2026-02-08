package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/attach/#functions-scripts
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Forces a base to display attachments as scripts.
 * 
 * 
 */
@SerialName("math.scripts")
data class TMathScripts(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The base to attach the scripts to.
     * 
     * Required, positional; Typst type: content
     */
    @all:Body @all:Positional val body: TContent,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.scripts")
    }
}
