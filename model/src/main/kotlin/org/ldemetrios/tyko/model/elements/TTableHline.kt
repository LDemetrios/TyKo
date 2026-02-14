package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/table/#definitions-hline
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-hline](https://typst.app/docs/reference/model/table/#definitions-hline)
 * 
 * A horizontal line in the table.
 * 
 * Overrides any per-cell stroke, including stroke specified through the table's `stroke` field. Can cross spacing between cells created through the table's [`column-gutter`](https://typst.app/docs/reference/model/table/#parameters-column-gutter) option.
 * 
 * Use this function instead of the table's `stroke` field if you want to manually place a horizontal line at a specific position in a single table. Consider using [table's `stroke`](https://typst.app/docs/reference/model/table/#parameters-stroke) field or [`table.cell`'s `stroke`](https://typst.app/docs/reference/model/table/#definitions-cell-stroke) field instead if the line you want to place is part of all your tables' designs.
 * 
 * 
 */
@SerialName("table.hline")
data class TTableHline(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-hline](https://typst.app/docs/reference/model/table/#definitions-hline)
     * 
     * The row above which the horizontal line is placed (zero-indexed). Functions identically to the `y` field in [`grid.hline`](https://typst.app/docs/reference/layout/grid/#definitions-hline-y).
     * 
     * Settable; Typst type: auto|int
     */
    @all:Settable val y: Smart<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-hline](https://typst.app/docs/reference/model/table/#definitions-hline)
     * 
     * The column at which the horizontal line starts (zero-indexed, inclusive).
     * 
     * Settable; Typst type: int
     */
    @all:Settable val start: TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-hline](https://typst.app/docs/reference/model/table/#definitions-hline)
     * 
     * The column before which the horizontal line ends (zero-indexed, exclusive).
     * 
     * Settable; Typst type: none|int
     */
    @all:Settable val end: Option<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-hline](https://typst.app/docs/reference/model/table/#definitions-hline)
     * 
     * The line's stroke.
     * 
     * Specifying `none` removes any lines previously placed across this line's range, including hlines or per-cell stroke below it.
     * 
     * Settable; Typst type: none|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Option<TStroke>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-hline](https://typst.app/docs/reference/model/table/#definitions-hline)
     * 
     * The position at which the line is placed, given its row (`y`) - either `top` to draw above it or `bottom` to draw below it.
     * 
     * This setting is only relevant when row gutter is enabled (and shouldn't be used otherwise - prefer just increasing the `y` field by one instead), since then the position below a row becomes different from the position above the next row due to the spacing between both.
     * 
     * Settable; Typst type: alignment
     */
    @all:Settable val position: TAlignment? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("table.hline")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TTableHline]
 */
@SerialName("set-table.hline")
data class TSetTableHline(
    override val internals: SetRuleInternals? = null,
    val y: Smart<TInt>? = null,
    val start: TInt? = null,
    val end: Option<TInt>? = null,
    val stroke: Option<TStroke>? = null,
    val position: TAlignment? = null
) : TSetRule()
