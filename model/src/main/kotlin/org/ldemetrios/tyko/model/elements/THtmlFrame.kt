package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/html/frame/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * An element that lays out its content as an inline SVG.
 * 
 * Sometimes, converting Typst content to HTML is not desirable. This can be the case for plots and other content that relies on positioning and styling to convey its message.
 * 
 * This function allows you to use the Typst layout engine that would also be used for PDF, SVG, and PNG export to render a part of your document exactly how it would appear when exported in one of these formats. It embeds the content as an inline SVG.
 */
@SerialName("html.frame")
data class THtmlFrame(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content that shall be laid out.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("html.frame")
    }
}
