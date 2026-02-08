package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/heading/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A section heading.
 * 
 * With headings, you can structure your document into sections. Each heading has a *level,* which starts at one and is unbounded upwards. This level indicates the logical role of the following content (section, subsection, etc.) A top-level heading indicates a top-level section of the document (not the document's title). To insert a title, use the [`title`](https://typst.app/docs/reference/model/title/) element instead.
 * 
 * Typst can automatically number your headings for you. To enable numbering, specify how you want your headings to be numbered with a [numbering pattern or function](https://typst.app/docs/reference/model/numbering/).
 * 
 * Independently of the numbering, Typst can also automatically generate an [outline](https://typst.app/docs/reference/model/outline/) of all headings for you. To exclude one or more headings from this outline, you can set the `outlined` parameter to `false`.
 * 
 * When writing a [show rule](https://typst.app/docs/reference/styling/#show-rules) that accesses the [`body` field](https://typst.app/docs/reference/model/heading/#parameters-body) to create a completely custom look for headings, make sure to wrap the content in a [`block`](https://typst.app/docs/reference/layout/block/) (which is implicitly [sticky](https://typst.app/docs/reference/layout/block/#parameters-sticky) for headings through a built-in show-set rule). This prevents headings from becoming "orphans", i.e. remaining at the end of the page with the following content being on the next page.
 * 
 * **_Example_**
 * 
 * ```typ
 * #set heading(numbering: "1.a)")
 * 
 * = Introduction
 * In recent years, ...
 * 
 * == Preliminaries
 * To start, ...
 * ```
 * <img src="https://typst.app/assets/docs/PajtbDMMN2eDYZCkAh9ZJwAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * Headings have dedicated syntax: They can be created by starting a line with one or multiple equals signs, followed by a space. The number of equals signs determines the heading's logical nesting depth. The `offset` field can be set to configure the starting depth.
 * 
 * **_Accessibility_**
 * 
 * Headings are important for accessibility, as they help users of Assistive Technologies (AT) like screen readers to navigate within your document. Screen reader users will be able to skip from heading to heading, or get an overview of all headings in the document.
 * 
 * To make your headings accessible, you should not skip heading levels. This means that you should start with a first-level heading. Also, when the previous heading was of level 3, the next heading should be of level 3 (staying at the same depth), level 4 (going exactly one level deeper), or level 1 or 2 (new hierarchically higher headings).
 * 
 * **_HTML export_**
 * 
 * As mentioned above, a top-level heading indicates a top-level section of the document rather than its title. This is in contrast to the HTML `<h1>` element of which there should be only one per document.
 * 
 * For this reason, in HTML export, a [`title`](https://typst.app/docs/reference/model/title/) element will turn into an `<h1>` and headings turn into `<h2>` and lower (a level 1 heading thus turns into `<h2>`, a level 2 heading into `<h3>`, etc).
 */
@SerialName("heading")
data class THeading(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The heading's title.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The absolute nesting depth of the heading, starting from one. If set to `auto`, it is computed from `offset + depth`.
     * 
     * This is primarily useful for usage in [show rules](https://typst.app/docs/reference/styling/#show-rules) (either with [`where`](https://typst.app/docs/reference/foundations/function/#definitions-where) selectors or by accessing the level directly on a shown heading).
     * 
     * Settable; Typst type: auto|int
     */
    @all:Settable val level: Smart<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The relative nesting depth of the heading, starting from one. This is combined with `offset` to compute the actual `level`.
     * 
     * This is set by the heading syntax, such that `== Heading` creates a heading with logical depth of 2, but actual level `offset + 2`. If you construct a heading manually, you should typically prefer this over setting the absolute level.
     * 
     * Settable; Typst type: int
     */
    @all:Settable val depth: TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The starting offset of each heading's `level`, used to turn its relative `depth` into its absolute `level`.
     * 
     * Settable; Typst type: int
     */
    @all:Settable val offset: TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * How to number the heading. Accepts a [numbering pattern or function](https://typst.app/docs/reference/model/numbering/) taking multiple numbers.
     * 
     * Settable; Typst type: none|str|function
     */
    @all:Settable val numbering: Option<Numbering>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * A supplement for the heading.
     * 
     * For references to headings, this is added before the referenced number.
     * 
     * If a function is specified, it is passed the referenced heading and should return content.
     * 
     * Settable; Typst type: none|auto|content|function
     */
    @all:Settable val supplement: Smart<Option<Computable<TContent>>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether the heading should appear in the [outline](https://typst.app/docs/reference/model/outline/).
     * 
     * Note that this property, if set to `true`, ensures the heading is also shown as a bookmark in the exported PDF's outline (when exporting to PDF). To change that behavior, use the `bookmarked` property.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val outlined: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether the heading should appear as a bookmark in the exported PDF's outline. Doesn't affect other export formats, such as PNG.
     * 
     * The default value of `auto` indicates that the heading will only appear in the exported PDF's outline if its `outlined` property is set to `true`, that is, if it would also be listed in Typst's [outline](https://typst.app/docs/reference/model/outline/). Setting this property to either `true` (bookmark) or `false` (don't bookmark) bypasses that behavior.
     * 
     * Settable; Typst type: auto|bool
     */
    @all:Settable val bookmarked: Smart<TBool>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The indent all but the first line of a heading should have.
     * 
     * The default value of `auto` uses the width of the numbering as indent if the heading is aligned at the [start](https://typst.app/docs/reference/layout/direction/#definitions-start) of the [text direction](https://typst.app/docs/reference/text/text/#parameters-dir), and no indent for center and other alignments.
     * 
     * Settable; Typst type: auto|length
     */
    @all:Settable val hangingIndent: Smart<TLength>? = null,
    override val label: TLabel? = null
) : TSelectableContent<THeading>() {
    override fun elem(): TLocatableElement<THeading> = ELEM

    companion object {
        val ELEM = TLocatableElement<THeading>("heading")
    }
}


@SerialName("set-heading")
data class TSetHeading(
    override val internals: SetRuleInternals? = null,
    val level: Smart<TInt>? = null,
    val depth: TInt? = null,
    val offset: TInt? = null,
    val numbering: Option<Numbering>? = null,
    val supplement: Smart<Option<Computable<TContent>>>? = null,
    val outlined: TBool? = null,
    val bookmarked: Smart<TBool>? = null,
    val hangingIndent: Smart<TLength>? = null
) : TSetRule()
