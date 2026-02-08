package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/emph/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Emphasizes content by toggling italics.
 * 
 * - If the current [text style](https://typst.app/docs/reference/text/text/#parameters-style) is `"normal"`, this turns it into `"italic"`.
 * - If it is already `"italic"` or `"oblique"`, it turns it back to `"normal"`.
 * 
 * **_Example_**
 * 
 * ```typ
 * This is _emphasized._ \
 * This is #emph[too.]
 * 
 * #show emph: it => {
 *   text(blue, it.body)
 * }
 * 
 * This is _emphasized_ differently.
 * ```
 * <img src="https://typst.app/assets/docs/p3cGCgaJdrkrScOita7QfgAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This function also has dedicated syntax: To emphasize content, simply enclose it in underscores (`_`). Note that this only works at word boundaries. To emphasize part of a word, you have to use the function.
 */
@SerialName("emph")
data class TEmph(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content to emphasize.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    override val label: TLabel? = null
) : TSelectableContent<TEmph>() {
    override fun elem(): TLocatableElement<TEmph> = ELEM

    companion object {
        val ELEM = TLocatableElement<TEmph>("emph")
    }
}
