package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/grid/#definitions-cell
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/grid/#definitions-cell](https://typst.app/docs/reference/layout/grid/#definitions-cell)
 * 
 * A cell in the grid. You can use this function in the argument list of a grid to override grid style properties for an individual cell or manually positioning it within the grid. You can also use this function in show rules to apply certain styles to multiple cells at once.
 * 
 * For example, you can override the position and stroke for a single cell:
 * 
 * You may also apply a show rule on `grid.cell` to style all cells at once, which allows you, for example, to apply styles based on a cell's position. Refer to the examples of the [`table.cell`](https://typst.app/docs/reference/model/table/#definitions-cell) element to learn more about this.
 */
@SerialName("grid.cell")
data class TGridCell(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/#definitions-cell](https://typst.app/docs/reference/layout/grid/#definitions-cell)
     * 
     * The cell's body.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/#definitions-cell](https://typst.app/docs/reference/layout/grid/#definitions-cell)
     * 
     * The cell's column (zero-indexed). This field may be used in show rules to style a cell depending on its column.
     * 
     * You may override this field to pick in which column the cell must be placed. If no row (`y`) is chosen, the cell will be placed in the first row (starting at row 0) with that column available (or a new row if none). If both `x` and `y` are chosen, however, the cell will be placed in that exact position. An error is raised if that position is not available (thus, it is usually wise to specify cells with a custom position before cells with automatic positions).
     * 
     * Settable; Typst type: auto|int
     */
    @all:Settable val x: Smart<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/#definitions-cell](https://typst.app/docs/reference/layout/grid/#definitions-cell)
     * 
     * The cell's row (zero-indexed). This field may be used in show rules to style a cell depending on its row.
     * 
     * You may override this field to pick in which row the cell must be placed. If no column (`x`) is chosen, the cell will be placed in the first column (starting at column 0) available in the chosen row. If all columns in the chosen row are already occupied, an error is raised.
     * 
     * Settable; Typst type: auto|int
     */
    @all:Settable val y: Smart<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/#definitions-cell](https://typst.app/docs/reference/layout/grid/#definitions-cell)
     * 
     * The amount of columns spanned by this cell.
     * 
     * Settable; Typst type: int
     */
    @all:Settable val colspan: TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/#definitions-cell](https://typst.app/docs/reference/layout/grid/#definitions-cell)
     * 
     * The amount of rows spanned by this cell.
     * 
     * Settable; Typst type: int
     */
    @all:Settable val rowspan: TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/#definitions-cell](https://typst.app/docs/reference/layout/grid/#definitions-cell)
     * 
     * The cell's [inset](https://typst.app/docs/reference/layout/grid/#parameters-inset) override.
     * 
     * Settable; Typst type: auto|relative|dictionary
     */
    @all:Settable val inset: Smart<Sides<TRelative>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/#definitions-cell](https://typst.app/docs/reference/layout/grid/#definitions-cell)
     * 
     * The cell's [alignment](https://typst.app/docs/reference/layout/grid/#parameters-align) override.
     * 
     * Settable; Typst type: auto|alignment
     */
    @all:Settable val align: Smart<TAlignment>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/#definitions-cell](https://typst.app/docs/reference/layout/grid/#definitions-cell)
     * 
     * The cell's [fill](https://typst.app/docs/reference/layout/grid/#parameters-fill) override.
     * 
     * Settable; Typst type: none|auto|color|gradient|tiling
     */
    @all:Settable val fill: Smart<Option<TPaint>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/#definitions-cell](https://typst.app/docs/reference/layout/grid/#definitions-cell)
     * 
     * The cell's [stroke](https://typst.app/docs/reference/layout/grid/#parameters-stroke) override.
     * 
     * Settable; Typst type: none|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Sides<Option<TStroke>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/#definitions-cell](https://typst.app/docs/reference/layout/grid/#definitions-cell)
     * 
     * Whether rows spanned by this cell can be placed in different pages. When equal to `auto`, a cell spanning only fixed-size rows is unbreakable, while a cell spanning at least one `auto`-sized row is breakable.
     * 
     * Settable; Typst type: auto|bool
     */
    @all:Settable val breakable: Smart<TBool>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("grid.cell")
    }
}


@SerialName("set-grid.cell")
data class TSetGridCell(
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
