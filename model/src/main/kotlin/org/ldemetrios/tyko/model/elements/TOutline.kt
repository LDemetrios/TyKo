package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/outline/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/outline/](https://typst.app/docs/reference/model/outline/)
 * 
 * A table of contents, figures, or other elements.
 * 
 * This function generates a list of all occurrences of an element in the document, up to a given [`depth`](https://typst.app/docs/reference/model/outline/#parameters-depth). The element's numbering and page number will be displayed in the outline alongside its title or caption.
 * 
 * **_Example_**
 * 
 * ```typ
 * #set heading(numbering: "1.")
 * #outline()
 * 
 * = Introduction
 * #lorem(5)
 * 
 * = Methods
 * == Setup
 * #lorem(10)
 * ```
 * <img src="https://typst.app/assets/docs/L428_MKlwIaf_QfRQG7msAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Alternative outlines_**
 * 
 * In its default configuration, this function generates a table of contents. By setting the `target` parameter, the outline can be used to generate a list of other kinds of elements than headings.
 * 
 * In the example below, we list all figures containing images by setting `target` to `figure.where(kind: image)`. Just the same, we could have set it to `figure.where(kind: table)` to generate a list of tables.
 * 
 * We could also set it to just `figure`, without using a [`where`](https://typst.app/docs/reference/foundations/function/#definitions-where) selector, but then the list would contain *all* figures, be it ones containing images, tables, or other material.
 * 
 * ```typ
 * #outline(
 *   title: [List of Figures],
 *   target: figure.where(kind: image),
 * )
 * 
 * #figure(
 *   image("tiger.jpg"),
 *   caption: [A nice figure!],
 * )
 * ```
 * <img src="https://typst.app/assets/docs/K0Fgir_M6IbOnlxFTpRoyAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Styling the outline_**
 * 
 * At the most basic level, you can style the outline by setting properties on it and its entries. This way, you can customize the outline's [title](https://typst.app/docs/reference/model/outline/#parameters-title), how outline entries are [indented](https://typst.app/docs/reference/model/outline/#parameters-indent), and how the space between an entry's text and its page number should be [filled](https://typst.app/docs/reference/model/outline/#definitions-entry-fill).
 * 
 * Richer customization is possible through configuration of the outline's [entries](https://typst.app/docs/reference/model/outline/#definitions-entry). The outline generates one entry for each outlined element.
 * 
 * **_Spacing the entries_**
 * 
 * Outline entries are [blocks](https://typst.app/docs/reference/layout/block/), so you can adjust the spacing between them with normal block-spacing rules:
 * 
 * ```typ
 * #show outline.entry.where(
 *   level: 1
 * ): set block(above: 1.2em)
 * 
 * #outline()
 * 
 * = About ACME Corp.
 * == History
 * === Origins
 * = Products
 * == ACME Tools
 * ```
 * <img src="https://typst.app/assets/docs/4FuZjZpq5YB7Y1J1b4I7bAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Building an outline entry from its parts_**
 * 
 * For full control, you can also write a transformational show rule on `outline.entry`. However, the logic for properly formatting and indenting outline entries is quite complex and the outline entry itself only contains two fields: The level and the outlined element.
 * 
 * For this reason, various helper functions are provided. You can mix and match these to compose an entry from just the parts you like.
 * 
 * The default show rule for an outline entry looks like this:
 * 
 * ```typ
 * #show outline.entry: it => link(
 *   it.element.location(),
 *   it.indented(it.prefix(), it.inner()),
 * )
 * ```
 * 
 * - The [`indented`](https://typst.app/docs/reference/model/outline/#definitions-entry-definitions-indented) function takes an optional prefix and inner content and automatically applies the proper indentation to it, such that different entries align nicely and long headings wrap properly.
 * - The [`prefix`](https://typst.app/docs/reference/model/outline/#definitions-entry-definitions-prefix) function formats the element's numbering (if any). It also appends a supplement for certain elements.
 * - The [`inner`](https://typst.app/docs/reference/model/outline/#definitions-entry-definitions-inner) function combines the element's [`body`](https://typst.app/docs/reference/model/outline/#definitions-entry-definitions-body), the filler, and the [`page` number](https://typst.app/docs/reference/model/outline/#definitions-entry-definitions-page).
 * 
 * You can use these individual functions to format the outline entry in different ways. Let's say, you'd like to fully remove the filler and page numbers. To achieve this, you could write a show rule like this:
 * 
 * ```typ
 * #show outline.entry: it => link(
 *   it.element.location(),
 *   // Keep just the body, dropping
 *   // the fill and the page.
 *   it.indented(it.prefix(), it.body()),
 * )
 * 
 * #outline()
 * 
 * = About ACME Corp.
 * == History
 * ```
 * <img src="https://typst.app/assets/docs/Hz_5qnr_Hshm4Lwem6fyiQAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("outline")
data class TOutline(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/outline/](https://typst.app/docs/reference/model/outline/)
     * 
     * The title of the outline.
     * 
     * - When set to `auto`, an appropriate title for the [text language](https://typst.app/docs/reference/text/text/#parameters-lang) will be used.
     * - When set to `none`, the outline will not have a title.
     * - A custom title can be set by passing content.
     * 
     * The outline's heading will not be numbered by default, but you can force it to be with a show-set rule: `show outline: set heading(numbering: "1.")`
     * 
     * Settable; Typst type: none|auto|content
     */
    @all:Settable val title: Smart<Option<TContent>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/outline/](https://typst.app/docs/reference/model/outline/)
     * 
     * The type of element to include in the outline.
     * 
     * To list figures containing a specific kind of element, like an image or a table, you can specify the desired kind in a [`where`](https://typst.app/docs/reference/foundations/function/#definitions-where) selector. See the section on [alternative outlines](https://typst.app/docs/reference/model/outline/#alternative-outlines) for more details.
     * 
     * Settable; Typst type: label|selector|location|function
     */
    @all:Settable val target: TSelector<*>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/outline/](https://typst.app/docs/reference/model/outline/)
     * 
     * The maximum level up to which elements are included in the outline. When this argument is `none`, all elements are included.
     * 
     * Settable; Typst type: none|int
     */
    @all:Settable val depth: Option<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/outline/](https://typst.app/docs/reference/model/outline/)
     * 
     * How to indent the outline's entries.
     * 
     * - `auto`: Indents the numbering/prefix of a nested entry with the title of its parent entry. If the entries are not numbered (e.g., via [heading numbering](https://typst.app/docs/reference/model/heading/#parameters-numbering)), this instead simply inserts a fixed amount of `1.2em` indent per level.
     * - [Relative length](https://typst.app/docs/reference/layout/relative/): Indents the entry by the specified length per nesting level. Specifying `2em`, for instance, would indent top-level headings by `0em` (not nested), second level headings by `2em` (nested once), third-level headings by `4em` (nested twice) and so on.
     * - [Function](https://typst.app/docs/reference/foundations/function/): You can further customize this setting with a function. That function receives the nesting level as a parameter (starting at 0 for top-level headings/elements) and should return a (relative) length. For example, `n => n * 2em` would be equivalent to just specifying `2em`.
     * 
     * Settable; Typst type: auto|relative|function
     */
    @all:Settable val indent: Smart<Computable<TRelative>>? = null,
    override val label: TLabel? = null
) : TSelectableContent<TOutline>() {
    override fun elem(): TLocatableElement<TOutline> = ELEM

    companion object {
        val ELEM = TLocatableElement<TOutline>("outline")
    }
}


@SerialName("set-outline")
data class TSetOutline(
    override val internals: SetRuleInternals? = null,
    val title: Smart<Option<TContent>>? = null,
    val target: TSelector<*>? = null,
    val depth: Option<TInt>? = null,
    val indent: Smart<Computable<TRelative>>? = null
) : TSetRule()
