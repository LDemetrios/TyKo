package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/title/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/title/](https://typst.app/docs/reference/model/title/)
 * 
 * A document title.
 * 
 * This should be used to display the main title of the whole document and should occur only once per document. In contrast, level 1 [headings](https://typst.app/docs/reference/model/heading/) are intended to be used for the top-level sections of the document.
 * 
 * Note that additional frontmatter (like an author list) that should appear together with the title does not belong in its body.
 * 
 * In HTML export, this shows as a `h1` element while level 1 headings show as `h2` elements.
 * 
 * **_Example_**
 * 
 * ```typ
 * #set document(
 *   title: [Interstellar Mail Delivery]
 * )
 * 
 * #title()
 * 
 * = Introduction
 * In recent years, ...
 * ```
 * <img src="https://typst.app/assets/docs/UVjmmYHJbHHQ-BVwZ_0XJgAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("title")
data class TTitle(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/title/](https://typst.app/docs/reference/model/title/)
     * 
     * The content of the title.
     * 
     * When omitted (or `auto`), this will default to [`document.title`](https://typst.app/docs/reference/model/document/#parameters-title). In this case, a document title must have been previously set with `set document(title: [..])`.
     * 
     * Positional, settable; Typst type: auto|content
     */
    @all:Settable @all:Positional val body: Smart<TContent>? = null,
    override val label: TLabel? = null
) : TSelectableContent<TTitle>() {
    override fun elem(): TLocatableElement<TTitle> = ELEM

    companion object {
        val ELEM = TLocatableElement<TTitle>("title")
    }
}


@SerialName("set-title")
data class TSetTitle(
    override val internals: SetRuleInternals? = null,
    @all:Positional val body: Smart<TContent>? = null
) : TSetRule()
