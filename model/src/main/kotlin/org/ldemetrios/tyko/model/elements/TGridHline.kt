package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/grid/#definitions-hline
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A horizontal line in the grid.
 * 
 * Overrides any per-cell stroke, including stroke specified through the grid's `stroke` field. Can cross spacing between cells created through the grid's `column-gutter` option.
 * 
 * An example for this function can be found at the [`table.hline`](https://typst.app/docs/reference/model/table/#definitions-hline) element.
 */
@SerialName("grid.hline")
data class TGridHline(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The row above which the horizontal line is placed (zero-indexed). If the `position` field is set to `bottom`, the line is placed below the row with the given index instead (see [`grid.hline.position`](https://typst.app/docs/reference/layout/grid/#definitions-hline-position) for details).
     * 
     * Specifying `auto` causes the line to be placed at the row below the last automatically positioned cell (that is, cell without coordinate overrides) before the line among the grid's children. If there is no such cell before the line, it is placed at the top of the grid (row 0). Note that specifying for this option exactly the total amount of rows in the grid causes this horizontal line to override the bottom border of the grid, while a value of 0 overrides the top border.
     * 
     * Settable; Typst type: auto|int
     */
    @all:Settable val y: Smart<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The column at which the horizontal line starts (zero-indexed, inclusive).
     * 
     * Settable; Typst type: int
     */
    @all:Settable val start: TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The column before which the horizontal line ends (zero-indexed, exclusive). Therefore, the horizontal line will be drawn up to and across column `end - 1`.
     * 
     * A value equal to `none` or to the amount of columns causes it to extend all the way towards the end of the grid.
     * 
     * Settable; Typst type: none|int
     */
    @all:Settable val end: Option<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The line's stroke.
     * 
     * Specifying `none` removes any lines previously placed across this line's range, including hlines or per-cell stroke below it.
     * 
     * Settable; Typst type: none|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Option<TStroke>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
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
        val ELEM = TElement("grid.hline")
    }
}


@SerialName("set-grid.hline")
data class TSetGridHline(
    override val internals: SetRuleInternals? = null,
    val y: Smart<TInt>? = null,
    val start: TInt? = null,
    val end: Option<TInt>? = null,
    val stroke: Option<TStroke>? = null,
    val position: TAlignment? = null
) : TSetRule()
