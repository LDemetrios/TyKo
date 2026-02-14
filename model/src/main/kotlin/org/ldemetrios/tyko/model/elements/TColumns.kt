package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/layout/columns/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/columns/](https://typst.app/docs/reference/layout/columns/)
 * 
 * Separates a region into multiple equally sized columns.
 * 
 * The `column` function lets you separate the interior of any container into multiple columns. It will currently not balance the height of the columns. Instead, the columns will take up the height of their container or the remaining height on the page. Support for balanced columns is planned for the future.
 * 
 * When arranging content across multiple columns, use [`colbreak`](https://typst.app/docs/reference/layout/colbreak/) to explicitly continue in the next column.
 * 
 * **_Example_**
 * 
 * ```typ
 * #columns(2, gutter: 8pt)[
 *   This text is in the
 *   first column.
 * 
 *   #colbreak()
 * 
 *   This text is in the
 *   second column.
 * ]
 * ```
 * <img src="https://typst.app/assets/docs/g7s0FDVA18XrIBkGtU4zDQAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Page-level columns_**
 * 
 * If you need to insert columns across your whole document, use the `page` function's [`columns` parameter](https://typst.app/docs/reference/layout/page/#parameters-columns) instead. This will create the columns directly at the page-level rather than wrapping all of your content in a layout container. As a result, things like [pagebreaks](https://typst.app/docs/reference/layout/pagebreak/), [footnotes](https://typst.app/docs/reference/model/footnote/), and [line numbers](https://typst.app/docs/reference/model/par/#definitions-line) will continue to work as expected. For more information, also read the [relevant part of the page setup guide](https://typst.app/docs/guides/page-setup/#columns).
 * 
 * **_Breaking out of columns_**
 * 
 * To temporarily break out of columns (e.g. for a paper's title), use parent-scoped floating placement:
 * 
 * ```typ
 * #set page(columns: 2, height: 150pt)
 * 
 * #place(
 *   top + center,
 *   scope: "parent",
 *   float: true,
 *   text(1.4em, weight: "bold")[
 *     My document
 *   ],
 * )
 * 
 * #lorem(40)
 * ```
 * <img src="https://typst.app/assets/docs/qNRmHdtNgs8qpE-RUR-XyQAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("columns")
data class TColumns(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/columns/](https://typst.app/docs/reference/layout/columns/)
     * 
     * The number of columns.
     * 
     * Positional, settable; Typst type: int
     */
    @all:Settable @all:Positional val count: TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/columns/](https://typst.app/docs/reference/layout/columns/)
     * 
     * The content that should be layouted into the columns.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/columns/](https://typst.app/docs/reference/layout/columns/)
     * 
     * The size of the gutter space between each column.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val gutter: TRelative? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("columns")
    }

    public constructor(
        body: TContent,
        gutter: TRelative? = null,
        label: TLabel? = null
    ) : this(null, body, gutter, label)
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TColumns]
 */
@SerialName("set-columns")
data class TSetColumns(
    override val internals: SetRuleInternals? = null,
    @all:Positional val count: TInt? = null,
    val gutter: TRelative? = null
) : TSetRule()
