package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/hide/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Hides content without affecting layout.
 * 
 * The `hide` function allows you to hide content while the layout still "sees" it. This is useful for creating blank space that is exactly as large as some content.
 * 
 * **_Example_**
 * 
 * ```typ
 * Hello Jane \
 * #hide[Hello] Joe
 * ```
 * <img src="https://typst.app/assets/docs/w0ioP6Ne87hOMXgpgPJirgAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Redaction_**
 * 
 * This function may also be useful for redacting content as its arguments are neither present visually nor accessible to Assistive Technology. That said, there can be *some* traces of the hidden content (such as a bookmarked heading in the PDF's Document Outline).
 * 
 * Note that, depending on the circumstances, it may be possible for content to be reverse engineered based on its size in the layout. We thus do not recommend using this function to hide highly sensitive information.
 */
@SerialName("hide")
data class THide(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content to hide.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("hide")
    }
}
