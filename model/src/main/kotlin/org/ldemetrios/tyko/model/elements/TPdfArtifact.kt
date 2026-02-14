package org.ldemetrios.tyko.model


//!https://typst.app/docs/reference/pdf/artifact/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/pdf/artifact/](https://typst.app/docs/reference/pdf/artifact/)
 * 
 * Marks content as a PDF artifact.
 * 
 * Artifacts are parts of the document that are not meant to be read by Assistive Technology (AT), such as screen readers. Typical examples include purely decorative images that do not contribute to the meaning of the document, watermarks, or repeated content such as page numbers.
 * 
 * Typst will automatically mark certain content, such as page headers, footers, backgrounds, and foregrounds, as artifacts. Likewise, paths and shapes are automatically marked as artifacts, but their content is not. Repetitions of table headers and footers are also marked as artifacts.
 * 
 * Once something is marked as an artifact, you cannot make any of its contents accessible again. If you need to mark only part of something as an artifact, you may need to use this function multiple times.
 * 
 * If you are unsure what constitutes an artifact, check the [Accessibility Guide](https://typst.app/docs/guides/accessibility/#artifacts).
 * 
 * In the future, this function may be moved out of the `pdf` module, making it possible to hide content in HTML export from AT.
 */
@SerialName("pdf.artifact")
data class TPdfArtifact(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/pdf/artifact/](https://typst.app/docs/reference/pdf/artifact/)
     * 
     * The content that is an artifact.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/pdf/artifact/](https://typst.app/docs/reference/pdf/artifact/)
     * 
     * The artifact kind.
     * 
     * This will govern how the PDF reader treats the artifact during reflow and content extraction (e.g. copy and paste).
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"header"` | Repeats on the top of each page. |
     * | `"footer"` | Repeats at the bottom of each page. |
     * | `"page"` | Not part of the document, but rather the page it is printed on. An example would be cut marks or color bars. |
     * | `"other"` | Other artifacts, including purely cosmetic content, backgrounds, watermarks, and repeated content. |
     * 
     * Settable; Typst type: str
     */
    @all:Settable val kind: TPdfArtifactKind? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("pdf.artifact")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TPdfArtifact]
 */
@SerialName("set-pdf.artifact")
data class TSetPdfArtifact(
    override val internals: SetRuleInternals? = null,
    val kind: TPdfArtifactKind? = null
) : TSetRule()
