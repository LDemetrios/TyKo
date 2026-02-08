package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/table/#definitions-cell
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A cell in the table. Use this to position a cell manually or to apply styling. To do the latter, you can either use the function to override the properties for a particular cell, or use it in show rules to apply certain styles to multiple cells at once.
 * 
 * Perhaps the most important use case of `table.cell` is to make a cell span multiple columns and/or rows with the `colspan` and `rowspan` fields.
 * 
 * For example, you can override the fill, alignment or inset for a single cell:
 * 
 * You may also apply a show rule on `table.cell` to style all cells at once. Combined with selectors, this allows you to apply styles based on a cell's position:
 * 
 * 
 */
@SerialName("table.cell")
data class TTableCell(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The cell's body.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The cell's column (zero-indexed). Functions identically to the `x` field in [`grid.cell`](https://typst.app/docs/reference/layout/grid/#definitions-cell).
     * 
     * Settable; Typst type: auto|int
     */
    @all:Settable val x: Smart<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The cell's row (zero-indexed). Functions identically to the `y` field in [`grid.cell`](https://typst.app/docs/reference/layout/grid/#definitions-cell).
     * 
     * Settable; Typst type: auto|int
     */
    @all:Settable val y: Smart<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The amount of columns spanned by this cell.
     * 
     * Settable; Typst type: int
     */
    @all:Settable val colspan: TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The amount of rows spanned by this cell.
     * 
     * Settable; Typst type: int
     */
    @all:Settable val rowspan: TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The cell's [inset](https://typst.app/docs/reference/model/table/#parameters-inset) override.
     * 
     * Settable; Typst type: auto|relative|dictionary
     */
    @all:Settable val inset: Smart<Sides<TRelative>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The cell's [alignment](https://typst.app/docs/reference/model/table/#parameters-align) override.
     * 
     * Settable; Typst type: auto|alignment
     */
    @all:Settable val align: Smart<TAlignment>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The cell's [fill](https://typst.app/docs/reference/model/table/#parameters-fill) override.
     * 
     * Settable; Typst type: none|auto|color|gradient|tiling
     */
    @all:Settable val fill: Smart<Option<TPaint>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The cell's [stroke](https://typst.app/docs/reference/model/table/#parameters-stroke) override.
     * 
     * Settable; Typst type: none|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Sides<Option<TStroke>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether rows spanned by this cell can be placed in different pages. When equal to `auto`, a cell spanning only fixed-size rows is unbreakable, while a cell spanning at least one `auto`-sized row is breakable.
     * 
     * Settable; Typst type: auto|bool
     */
    @all:Settable val breakable: Smart<TBool>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("table.cell")
    }
}


@SerialName("set-table.cell")
data class TSetTableCell(
    override val internals: SetRuleInternals? = null,
    val x: Smart<TInt>? = null,
    val y: Smart<TInt>? = null,
    val colspan: TInt? = null,
    val rowspan: TInt? = null,
    val inset: Smart<Sides<TRelative>>? = null,
    val align: Smart<TAlignment>? = null,
    val fill: Smart<Option<TPaint>>? = null,
    val stroke: Sides<Option<TStroke>>? = null,
    val breakable: Smart<TBool>? = null
) : TSetRule()
