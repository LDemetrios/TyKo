package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/grid/#definitions-vline
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A vertical line in the grid.
 * 
 * Overrides any per-cell stroke, including stroke specified through the grid's `stroke` field. Can cross spacing between cells created through the grid's `row-gutter` option.
 */
@SerialName("grid.vline")
data class TGridVline(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The column before which the vertical line is placed (zero-indexed). If the `position` field is set to `end`, the line is placed after the column with the given index instead (see [`grid.vline.position`](https://typst.app/docs/reference/layout/grid/#definitions-vline-position) for details).
     * 
     * Specifying `auto` causes the line to be placed at the column after the last automatically positioned cell (that is, cell without coordinate overrides) before the line among the grid's children. If there is no such cell before the line, it is placed before the grid's first column (column 0). Note that specifying for this option exactly the total amount of columns in the grid causes this vertical line to override the end border of the grid (right in LTR, left in RTL), while a value of 0 overrides the start border (left in LTR, right in RTL).
     * 
     * Settable; Typst type: auto|int
     */
    @all:Settable val x: Smart<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The row at which the vertical line starts (zero-indexed, inclusive).
     * 
     * Settable; Typst type: int
     */
    @all:Settable val start: TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The row on top of which the vertical line ends (zero-indexed, exclusive). Therefore, the vertical line will be drawn up to and across row `end - 1`.
     * 
     * A value equal to `none` or to the amount of rows causes it to extend all the way towards the bottom of the grid.
     * 
     * Settable; Typst type: none|int
     */
    @all:Settable val end: Option<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The line's stroke.
     * 
     * Specifying `none` removes any lines previously placed across this line's range, including vlines or per-cell stroke below it.
     * 
     * Settable; Typst type: none|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Option<TStroke>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The position at which the line is placed, given its column (`x`) - either `start` to draw before it or `end` to draw after it.
     * 
     * The values `left` and `right` are also accepted, but discouraged as they cause your grid to be inconsistent between left-to-right and right-to-left documents.
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
        val ELEM = TElement("grid.vline")
    }
}


@SerialName("set-grid.vline")
data class TSetGridVline(
    override val internals: SetRuleInternals? = null,
    val x: Smart<TInt>? = null,
    val start: TInt? = null,
    val end: Option<TInt>? = null,
    val stroke: Option<TStroke>? = null,
    val position: TAlignment? = null
) : TSetRule()
