package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/footnote/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A footnote.
 * 
 * Includes additional remarks and references on the same page with footnotes. A footnote will insert a superscript number that links to the note at the bottom of the page. Notes are numbered sequentially throughout your document and can break across multiple pages.
 * 
 * To customize the appearance of the entry in the footnote listing, see [`footnote.entry`](https://typst.app/docs/reference/model/footnote/#definitions-entry). The footnote itself is realized as a normal superscript, so you can use a set rule on the [`super`](https://typst.app/docs/reference/text/super/) function to customize it. You can also apply a show rule to customize only the footnote marker (superscript number) in the running text.
 * 
 * **_Example_**
 * 
 * ```typ
 * Check the docs for more details.
 * #footnote[https://typst.app/docs]
 * ```
 * <img src="https://typst.app/assets/docs/Rux1n4IPwOkOpn1s57WxpAAAAAAAAAAA.png" alt="Preview" />
 * 
 * The footnote automatically attaches itself to the preceding word, even if there is a space before it in the markup. To force space, you can use the string `#" "` or explicit [horizontal spacing](https://typst.app/docs/reference/layout/h/).
 * 
 * By giving a label to a footnote, you can have multiple references to it.
 * 
 * ```typ
 * You can edit Typst documents online.
 * #footnote[https://typst.app/app] <fn>
 * Checkout Typst's website. @fn
 * And the online app. #footnote(<fn>)
 * ```
 * <img src="https://typst.app/assets/docs/xECSHtr0VhzFh5onpw48GQAAAAAAAAAA.png" alt="Preview" />
 * 
 * *Note:* Set and show rules in the scope where `footnote` is called may not apply to the footnote's content. See [here](https://github.com/typst/typst/issues/1467#issuecomment-1588799440) for more information.
 * 
 * **_Accessibility_**
 * 
 * Footnotes will be read by Assistive Technology (AT) immediately after the spot in the text where they are referenced, just like how they appear in markup.
 */
@SerialName("footnote")
data class TFootnote(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content to put into the footnote. Can also be the label of another footnote this one should point to.
     * 
     * Required, positional; Typst type: label|content
     */
    @all:Positional val body: Attribution,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * How to number footnotes. Accepts a [numbering pattern or function](https://typst.app/docs/reference/model/numbering/) taking a single number.
     * 
     * By default, the footnote numbering continues throughout your document. If you prefer per-page footnote numbering, you can reset the footnote [counter](https://typst.app/docs/reference/introspection/counter/) in the page [header](https://typst.app/docs/reference/layout/page/#parameters-header). In the future, there might be a simpler way to achieve this.
     * 
     * Settable; Typst type: str|function
     */
    @all:Settable val numbering: Numbering? = null,
    override val label: TLabel? = null
) : TSelectableContent<TFootnote>() {
    override fun elem(): TLocatableElement<TFootnote> = ELEM

    companion object {
        val ELEM = TLocatableElement<TFootnote>("footnote")
    }
}


@SerialName("set-footnote")
data class TSetFootnote(
    override val internals: SetRuleInternals? = null,
    val numbering: Numbering? = null
) : TSetRule()
