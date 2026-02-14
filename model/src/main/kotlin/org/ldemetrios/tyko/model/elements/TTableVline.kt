package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/table/#definitions-vline
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-vline](https://typst.app/docs/reference/model/table/#definitions-vline)
 * 
 * A vertical line in the table. See the docs for [`grid.vline`](https://typst.app/docs/reference/layout/grid/#definitions-vline) for more information regarding how to use this element's fields.
 * 
 * Overrides any per-cell stroke, including stroke specified through the table's `stroke` field. Can cross spacing between cells created through the table's [`row-gutter`](https://typst.app/docs/reference/model/table/#parameters-row-gutter) option.
 * 
 * Similar to [`table.hline`](https://typst.app/docs/reference/model/table/#definitions-hline), use this function if you want to manually place a vertical line at a specific position in a single table and use the [table's `stroke`](https://typst.app/docs/reference/model/table/#parameters-stroke) field or [`table.cell`'s `stroke`](https://typst.app/docs/reference/model/table/#definitions-cell-stroke) field instead if the line you want to place is part of all your tables' designs.
 */
@SerialName("table.vline")
data class TTableVline(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-vline](https://typst.app/docs/reference/model/table/#definitions-vline)
     * 
     * The column before which the vertical line is placed (zero-indexed). Functions identically to the `x` field in [`grid.vline`](https://typst.app/docs/reference/layout/grid/#definitions-vline).
     * 
     * Settable; Typst type: auto|int
     */
    @all:Settable val x: Smart<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-vline](https://typst.app/docs/reference/model/table/#definitions-vline)
     * 
     * The row at which the vertical line starts (zero-indexed, inclusive).
     * 
     * Settable; Typst type: int
     */
    @all:Settable val start: TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-vline](https://typst.app/docs/reference/model/table/#definitions-vline)
     * 
     * The row on top of which the vertical line ends (zero-indexed, exclusive).
     * 
     * Settable; Typst type: none|int
     */
    @all:Settable val end: Option<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-vline](https://typst.app/docs/reference/model/table/#definitions-vline)
     * 
     * The line's stroke.
     * 
     * Specifying `none` removes any lines previously placed across this line's range, including vlines or per-cell stroke below it.
     * 
     * Settable; Typst type: none|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Option<TStroke>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-vline](https://typst.app/docs/reference/model/table/#definitions-vline)
     * 
     * The position at which the line is placed, given its column (`x`) - either `start` to draw before it or `end` to draw after it.
     * 
     * The values `left` and `right` are also accepted, but discouraged as they cause your table to be inconsistent between left-to-right and right-to-left documents.
     * 
     * This setting is only relevant when column gutter is enabled (and shouldn't be used otherwise - prefer just increasing the `x` field by one instead), since then the position after a column becomes different from the position before the next column due to the spacing between both.
     * 
     * Settable; Typst type: alignment
     */
    @all:Settable val position: TAlignment? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("table.vline")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TTableVline]
 */
@SerialName("set-table.vline")
data class TSetTableVline(
    override val internals: SetRuleInternals? = null,
    val x: Smart<TInt>? = null,
    val start: TInt? = null,
    val end: Option<TInt>? = null,
    val stroke: Option<TStroke>? = null,
    val position: TAlignment? = null
) : TSetRule()
