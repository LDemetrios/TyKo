package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.Numbering
import org.ldemetrios.tyko.model.TPaint


//!https://typst.app/docs/reference/layout/page/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
 * 
 * Layouts its child onto one or multiple pages.
 * 
 * Although this function is primarily used in set rules to affect page properties, it can also be used to explicitly render its argument onto a set of pages of its own.
 * 
 * Pages can be set to use `auto` as their width or height. In this case, the pages will grow to fit their content on the respective axis.
 * 
 * The [Guide for Page Setup](https://typst.app/docs/guides/page-setup/) explains how to use this and related functions to set up a document with many examples.
 * 
 * **_Example_**
 * 
 * ```typ
 * #set page("us-letter")
 * 
 * There you go, US friends!
 * ```
 * <img src="https://typst.app/assets/docs/Gsn3vxGfYJJE0DFa5w6toQAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Accessibility_**
 * 
 * The contents of the page's header, footer, foreground, and background are invisible to Assistive Technology (AT) like screen readers. Only the body of the page is read by AT. Do not include vital information not included elsewhere in the document in these areas.
 */
@SerialName("page")
data class TPage(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * A standard paper size to set width and height.
     * 
     * This is just a shorthand for setting `width` and `height` and, as such, cannot be retrieved in a context expression.
     * 
     * Typst type: str
     */
    val paper: TStr? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * The width of the page.
     * 
     * Settable; Typst type: auto|length
     */
    @all:Settable val width: Smart<Option<TLength>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * The height of the page.
     * 
     * If this is set to `auto`, page breaks can only be triggered manually by inserting a [page break](https://typst.app/docs/reference/layout/pagebreak/) or by adding another non-empty page set rule. Most examples throughout this documentation use `auto` for the height of the page to dynamically grow and shrink to fit their content.
     * 
     * Settable; Typst type: auto|length
     */
    @all:Settable val height: Smart<Option<TLength>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * Whether the page is flipped into landscape orientation.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val flipped: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * The page's margins.
     * 
     * - `auto`: The margins are set automatically to 2.5/21 times the smaller dimension of the page. This results in 2.5 cm margins for an A4 page.
     * - A single length: The same margin on all sides.
     * - A dictionary: With a dictionary, the margins can be set individually. The dictionary can contain the following keys in order of precedence:
     *   - `top`: The top margin.
     *   - `right`: The right margin.
     *   - `bottom`: The bottom margin.
     *   - `left`: The left margin.
     *   - `inside`: The margin at the inner side of the page (where the [binding](https://typst.app/docs/reference/layout/page/#parameters-binding) is).
     *   - `outside`: The margin at the outer side of the page (opposite to the [binding](https://typst.app/docs/reference/layout/page/#parameters-binding)).
     *   - `x`: The horizontal margins.
     *   - `y`: The vertical margins.
     *   - `rest`: The margins on all sides except those for which the dictionary explicitly sets a size.
     * 
     * All keys are optional; omitted keys will use their previously set value, or the default margin if never set. In addition, the values for `left` and `right` are mutually exclusive with the values for `inside` and `outside`.
     * 
     * Settable; Typst type: auto|relative|dictionary
     */
    @all:Settable val margin: Margin<Smart<TRelative>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * On which side the pages will be bound.
     * 
     * - `auto`: Equivalent to `left` if the [text direction](https://typst.app/docs/reference/text/text/#parameters-dir) is left-to-right and `right` if it is right-to-left.
     * - `left`: Bound on the left side.
     * - `right`: Bound on the right side.
     * 
     * This affects the meaning of the `inside` and `outside` options for margins.
     * 
     * Settable; Typst type: auto|alignment
     */
    @all:Settable val binding: Smart<TAlignment>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * How many columns the page has.
     * 
     * If you need to insert columns into a page or other container, you can also use the [`columns` function](https://typst.app/docs/reference/layout/columns/).
     * 
     * Settable; Typst type: int
     */
    @all:Settable val columns: TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * The page's background fill.
     * 
     * Setting this to something non-transparent instructs the printer to color the complete page. If you are considering larger production runs, it may be more environmentally friendly and cost-effective to source pre-dyed pages and not set this property.
     * 
     * When set to `none`, the background becomes transparent. Note that PDF pages will still appear with a (usually white) background in viewers, but they are actually transparent. (If you print them, no color is used for the background.)
     * 
     * The default of `auto` results in `none` for PDF output, and `white` for PNG and SVG.
     * 
     * Settable; Typst type: none|auto|color|gradient|tiling
     */
    @all:Settable val fill: Smart<Option<TPaint>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * How to number the pages. You can refer to the Page Setup Guide for [customizing page numbers](https://typst.app/docs/guides/page-setup/#page-numbers).
     * 
     * Accepts a [numbering pattern or function](https://typst.app/docs/reference/model/numbering/) taking one or two numbers:
     * 
     * 1. The first number is the current page number.
     * 1. The second number is the total number of pages. In a numbering pattern, the second number can be omitted. If a function is passed, it will receive one argument in the context of links or references, and two arguments when producing the visible page numbers.
     * 
     * These are logical numbers controlled by the page counter, and may thus not match the physical numbers. Specifically, they are the [current](https://typst.app/docs/reference/introspection/counter/#definitions-get) and the [final](https://typst.app/docs/reference/introspection/counter/#definitions-final) value of `counter(page)`. See the [`counter`](https://typst.app/docs/reference/introspection/counter/#page-counter) documentation for more details.
     * 
     * If an explicit [`footer`](https://typst.app/docs/reference/layout/page/#parameters-footer) (or [`header`](https://typst.app/docs/reference/layout/page/#parameters-header) for [top-aligned](https://typst.app/docs/reference/layout/page/#parameters-number-align) numbering) is given, the numbering is ignored.
     * 
     * Settable; Typst type: none|str|function
     */
    @all:Settable val numbering: Option<Numbering>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * A supplement for the pages.
     * 
     * For page references, this is added before the page number.
     * 
     * Settable; Typst type: none|auto|content
     */
    @all:Settable val supplement: Smart<Option<TContent>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * The alignment of the page numbering.
     * 
     * If the vertical component is `top`, the numbering is placed into the header and if it is `bottom`, it is placed in the footer. Horizon alignment is forbidden. If an explicit matching `header` or `footer` is given, the numbering is ignored.
     * 
     * Settable; Typst type: alignment
     */
    @all:Settable val numberAlign: TAlignment? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * The page's header. Fills the top margin of each page.
     * 
     * - Content: Shows the content as the header.
     * - `auto`: Shows the page number if a [`numbering`](https://typst.app/docs/reference/layout/page/#parameters-numbering) is set and [`number-align`](https://typst.app/docs/reference/layout/page/#parameters-number-align) is `top`.
     * - `none`: Suppresses the header.
     * 
     * Settable; Typst type: none|auto|content
     */
    @all:Settable val header: Smart<Option<TContent>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * The amount the header is raised into the top margin.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val headerAscent: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * The page's footer. Fills the bottom margin of each page.
     * 
     * - Content: Shows the content as the footer.
     * - `auto`: Shows the page number if a [`numbering`](https://typst.app/docs/reference/layout/page/#parameters-numbering) is set and [`number-align`](https://typst.app/docs/reference/layout/page/#parameters-number-align) is `bottom`.
     * - `none`: Suppresses the footer.
     * 
     * For just a page number, the `numbering` property typically suffices. If you want to create a custom footer but still display the page number, you can directly access the [page counter](https://typst.app/docs/reference/introspection/counter/).
     * 
     * Settable; Typst type: none|auto|content
     */
    @all:Settable val footer: Smart<Option<TContent>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * The amount the footer is lowered into the bottom margin.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val footerDescent: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * Content in the page's background.
     * 
     * This content will be placed behind the page's body. It can be used to place a background image or a watermark.
     * 
     * Settable; Typst type: none|content
     */
    @all:Settable val background: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * Content in the page's foreground.
     * 
     * This content will overlay the page's body.
     * 
     * Settable; Typst type: none|content
     */
    @all:Settable val foreground: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/page/](https://typst.app/docs/reference/layout/page/)
     * 
     * The contents of the page(s).
     * 
     * Multiple pages will be created if the content does not fit on a single page. A new page with the page properties prior to the function invocation will be created after the body has been typeset.
     * 
     * Typst type: content
     */
    val body: TContent? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("page")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TPage]
 */
@SerialName("set-page")
data class TSetPage(
    override val internals: SetRuleInternals? = null,
    val width: Smart<Option<TLength>>? = null,
    val height: Smart<Option<TLength>>? = null,
    val flipped: TBool? = null,
    val margin: Margin<Smart<TRelative>>? = null,
    val binding: Smart<TAlignment>? = null,
    val columns: TInt? = null,
    val fill: Smart<Option<TPaint>>? = null,
    val numbering: Option<Numbering>? = null,
    val supplement: Smart<Option<TContent>>? = null,
    val numberAlign: TAlignment? = null,
    val header: Smart<Option<TContent>>? = null,
    val headerAscent: TRelative? = null,
    val footer: Smart<Option<TContent>>? = null,
    val footerDescent: TRelative? = null,
    val background: Option<TContent>? = null,
    val foreground: Option<TContent>? = null
) : TSetRule()
