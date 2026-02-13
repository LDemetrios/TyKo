package org.ldemetrios.tyko.model


//!https://typst.app/docs/reference/model/document/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/document/](https://typst.app/docs/reference/model/document/)
 * 
 * The root element of a document and its metadata.
 * 
 * All documents are automatically wrapped in a `document` element. You cannot create a document element yourself. This function is only used with [set rules](https://typst.app/docs/reference/styling/#set-rules) to specify document metadata. Such a set rule must not occur inside of any layout container.
 * 
 * ```typ
 * #set document(title: [Hello])
 * 
 * This has no visible output, but
 * embeds metadata into the PDF!
 * ```
 * <img src="https://typst.app/assets/docs/g-R4wkXOtFnr5QmDRHynVAAAAAAAAAAA.png" alt="Preview" />
 * 
 * Note that metadata set with this function is not rendered within the document. Instead, it is embedded in the compiled PDF file.
 */
@SerialName("document")
data class TDocument(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/document/](https://typst.app/docs/reference/model/document/)
     * 
     * The document's title. This is rendered as the title of the PDF viewer window or the browser tab of the page.
     * 
     * Adding a title is important for accessibility, as it makes it easier to navigate to your document and identify it among other open documents. When exporting to PDF/UA, a title is required.
     * 
     * While this can be arbitrary content, PDF viewers only support plain text titles, so the conversion might be lossy.
     * 
     * Settable; Typst type: none|content
     */
    @all:Settable val title: Option<TAttachment>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/document/](https://typst.app/docs/reference/model/document/)
     * 
     * The document's authors.
     * 
     * Settable; Typst type: str|array
     */
    @all:Settable val author: ArrayOrSingle<TStr>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/document/](https://typst.app/docs/reference/model/document/)
     * 
     * The document's description.
     * 
     * Settable; Typst type: none|content
     */
    @all:Settable val description: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/document/](https://typst.app/docs/reference/model/document/)
     * 
     * The document's keywords.
     * 
     * Settable; Typst type: str|array
     */
    @all:Settable val keywords:  ArrayOrSingle<TStr>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/document/](https://typst.app/docs/reference/model/document/)
     * 
     * The document's creation date.
     * 
     * If this is `auto` (default), Typst uses the current date and time. Setting it to `none` prevents Typst from embedding any creation date into the PDF metadata.
     * 
     * The year component must be at least zero in order to be embedded into a PDF.
     * 
     * If you want to create byte-by-byte reproducible PDFs, set this to something other than `auto`.
     * 
     * Settable; Typst type: none|auto|datetime
     */
    @all:Settable val date: Smart<Option<TDatetime>>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("document")
    }
}


@SerialName("set-document")
data class TSetDocument(
    override val internals: SetRuleInternals? = null,
    val title: Option<TAttachment>? = null,
    val author: ArrayOrSingle<TStr>? = null,
    val description: Option<TContent>? = null,
    val keywords: ArrayOrSingle<TStr>? = null,
    val date: Smart<Option<TDatetime>>? = null
) : TSetRule()
