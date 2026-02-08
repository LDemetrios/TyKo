package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/ref/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A reference to a label or bibliography.
 * 
 * Takes a label and cross-references it. There are two kind of references, determined by its [`form`](https://typst.app/docs/reference/model/ref/#parameters-form): `"normal"` and `"page"`.
 * 
 * The default, a `"normal"` reference, produces a textual reference to a label. For example, a reference to a heading will yield an appropriate string such as "Section 1" for a reference to the first heading. The word "Section" depends on the [`lang`](https://typst.app/docs/reference/text/text/#parameters-lang) setting and is localized accordingly. The references are also links to the respective element. Reference syntax can also be used to [cite](https://typst.app/docs/reference/model/cite/) from a bibliography.
 * 
 * As the default form requires a supplement and numbering, the label must be attached to a *referenceable element*. Referenceable elements include [headings](https://typst.app/docs/reference/model/heading/), [figures](https://typst.app/docs/reference/model/figure/), [equations](https://typst.app/docs/reference/math/equation/), and [footnotes](https://typst.app/docs/reference/model/footnote/). To create a custom referenceable element like a theorem, you can create a figure of a custom [`kind`](https://typst.app/docs/reference/model/figure/#parameters-kind) and write a show rule for it. In the future, there might be a more direct way to define a custom referenceable element.
 * 
 * If you just want to link to a labelled element and not get an automatic textual reference, consider using the [`link`](https://typst.app/docs/reference/model/link/) function instead.
 * 
 * A `"page"` reference produces a page reference to a label, displaying the page number at its location. You can use the [page's supplement](https://typst.app/docs/reference/layout/page/#parameters-supplement) to modify the text before the page number. Unlike a `"normal"` reference, the label can be attached to any element.
 * 
 * **_Example_**
 * 
 * ```typ
 * #set page(numbering: "1")
 * #set heading(numbering: "1.")
 * #set math.equation(numbering: "(1)")
 * 
 * = Introduction <intro>
 * Recent developments in
 * typesetting software have
 * rekindled hope in previously
 * frustrated researchers. @distress
 * As shown in @results (see
 * #ref(<results>, form: "page")),
 * we ...
 * 
 * = Results <results>
 * We discuss our approach in
 * comparison with others.
 * 
 * == Performance <perf>
 * @slow demonstrates what slow
 * software looks like.
 * $ T(n) = O(2^n) $ <slow>
 * 
 * #bibliography("works.bib")
 * ```
 * <img src="https://typst.app/assets/docs/p5J0XM7JRakHYxONTkV2LwAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This function also has dedicated syntax: A `"normal"` reference to a label can be created by typing an `@` followed by the name of the label (e.g. `= Introduction <intro>` can be referenced by typing `@intro`).
 * 
 * To customize the supplement, add content in square brackets after the reference: `@intro[Chapter]`.
 * 
 * **_Customization_**
 * 
 * When you only ever need to reference pages of a figure/table/heading/etc. in a document, the default `form` field value can be changed to `"page"` with a set rule. If you prefer a short "p." supplement over "page", the [`page.supplement`](https://typst.app/docs/reference/layout/page/#parameters-supplement) field can be used for changing this:
 * 
 * ```typ
 * #set page(
 *   numbering: "1",
 *   supplement: "p.",
 * )
 * #set ref(form: "page")
 * 
 * #figure(
 *   stack(
 *     dir: ltr,
 *     spacing: 1em,
 *     circle(),
 *     square(),
 *   ),
 *   caption: [Shapes],
 * ) <shapes>
 * 
 * #pagebreak()
 * 
 * See @shapes for examples
 * of different shapes.
 * ```
 * <img src="https://typst.app/assets/docs/gJ5d2PrJIq0UrYQDXT3piQAAAAAAAAAA.png" alt="Preview" /><img src="https://typst.app/assets/docs/gJ5d2PrJIq0UrYQDXT3piQAAAAAAAAAB.png" alt="Preview" />
 * 
 * If you write a show rule for references, you can access the referenced element through the `element` field of the reference. The `element` may be `none` even if it exists if Typst hasn't discovered it yet, so you always need to handle that case in your code.
 * 
 * ```typ
 * #set heading(numbering: "1.")
 * #set math.equation(numbering: "(1)")
 * 
 * #show ref: it => {
 *   let eq = math.equation
 *   let el = it.element
 *   // Skip all other references.
 *   if el == none or el.func() != eq { return it }
 *   // Override equation references.
 *   link(el.location(), numbering(
 *     el.numbering,
 *     ..counter(eq).at(el.location())
 *   ))
 * }
 * 
 * = Beginnings <beginning>
 * In @beginning we prove @pythagoras.
 * $ a^2 + b^2 = c^2 $ <pythagoras>
 * ```
 * <img src="https://typst.app/assets/docs/RrkCbtiAxLAjFha9UP4x1gAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("ref")
data class TRef(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The target label that should be referenced.
     * 
     * Can be a label that is defined in the document or, if the [`form`](https://typst.app/docs/reference/model/ref/#parameters-form) is set to `"normal"`, an entry from the [`bibliography`](https://typst.app/docs/reference/model/bibliography/).
     * 
     * Required, positional; Typst type: label
     */
    @all:Positional val target: TLabel,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * A supplement for the reference.
     * 
     * If the [`form`](https://typst.app/docs/reference/model/ref/#parameters-form) is set to `"normal"`:
     * 
     * - For references to headings or figures, this is added before the referenced number.
     * - For citations, this can be used to add a page number.
     * 
     * If the [`form`](https://typst.app/docs/reference/model/ref/#parameters-form) is set to `"page"`, then this is added before the page number of the label referenced.
     * 
     * If a function is specified, it is passed the referenced element and should return content.
     * 
     * Settable; Typst type: none|auto|content|function
     */
    @all:Settable val supplement: Smart<Option<Computable<TContent>>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The kind of reference to produce.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"normal"` | Produces a textual reference to a label. |
     * | `"page"` | Produces a page reference to a label. |
     * 
     * Settable; Typst type: str
     */
    @all:Settable val form: TStr? = null,
    override val label: TLabel? = null
) : TSelectableContent<TRef>() {
    override fun elem(): TLocatableElement<TRef> = ELEM

    companion object {
        val ELEM = TLocatableElement<TRef>("ref")
    }
}


@SerialName("set-ref")
data class TSetRef(
    override val internals: SetRuleInternals? = null,
    val supplement: Smart<Option<Computable<TContent>>>? = null,
    val form: TStr? = null
) : TSetRule()
