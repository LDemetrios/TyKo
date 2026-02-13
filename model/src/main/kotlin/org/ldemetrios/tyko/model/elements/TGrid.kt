package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

import javax.swing.GroupLayout



//!https://typst.app/docs/reference/layout/grid/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/grid/](https://typst.app/docs/reference/layout/grid/)
 * 
 * Arranges content in a grid.
 * 
 * The grid element allows you to arrange content in a grid. You can define the number of rows and columns, as well as the size of the gutters between them. There are multiple sizing modes for columns and rows that can be used to create complex layouts.
 * 
 * While the grid and table elements work very similarly, they are intended for different use cases and carry different semantics. The grid element is intended for presentational and layout purposes, while the [`table`](https://typst.app/docs/reference/model/table/) element is intended for, in broad terms, presenting multiple related data points. Set and show rules on one of these elements do not affect the other. Refer to the [Accessibility Section](https://typst.app/docs/reference/layout/grid/#accessibility) to learn how grids and tables are presented to users of Assistive Technology (AT) like screen readers.
 * 
 * **_Sizing the tracks_**
 * 
 * A grid's sizing is determined by the track sizes specified in the arguments. There are multiple sizing parameters: [`columns`](https://typst.app/docs/reference/layout/grid/#parameters-columns), [`rows`](https://typst.app/docs/reference/layout/grid/#parameters-rows) and [`gutter`](https://typst.app/docs/reference/layout/grid/#parameters-gutter). Because each of the sizing parameters accepts the same values, we will explain them just once, here. Each sizing argument accepts an array of individual track sizes. A track size is either:
 * 
 * - `auto`: The track will be sized to fit its contents. It will be at most as large as the remaining space. If there is more than one `auto` track width, and together they claim more than the available space, the `auto` tracks will fairly distribute the available space among themselves.
 * - A fixed or relative length (e.g. `10pt` or `20% - 1cm`): The track will be exactly of this size.
 * - A fractional length (e.g. `1fr`): Once all other tracks have been sized, the remaining space will be divided among the fractional tracks according to their fractions. For example, if there are two fractional tracks, each with a fraction of `1fr`, they will each take up half of the remaining space.
 * 
 * To specify a single track, the array can be omitted in favor of a single value. To specify multiple `auto` tracks, enter the number of tracks instead of an array. For example, `columns:``3` is equivalent to `columns:``(auto, auto, auto)`.
 * 
 * **_Examples_**
 * 
 * The example below demonstrates the different track sizing options. It also shows how you can use [`grid.cell`](https://typst.app/docs/reference/layout/grid/#definitions-cell) to make an individual cell span two grid tracks.
 * 
 * ```typ
 * // We use `rect` to emphasize the
 * // area of cells.
 * #set rect(
 *   inset: 8pt,
 *   fill: rgb("e4e5ea"),
 *   width: 100%,
 * )
 * 
 * #grid(
 *   columns: (60pt, 1fr, 2fr),
 *   rows: (auto, 60pt),
 *   gutter: 3pt,
 *   rect[Fixed width, auto height],
 *   rect[1/3 of the remains],
 *   rect[2/3 of the remains],
 *   rect(height: 100%)[Fixed height],
 *   grid.cell(
 *     colspan: 2,
 *     image("tiger.jpg", width: 100%),
 *   ),
 * )
 * ```
 * <img src="https://typst.app/assets/docs/nU6HFHUP8AJwyw_E8LwJrgAAAAAAAAAA.png" alt="Preview" />
 * 
 * You can also [spread](https://typst.app/docs/reference/foundations/arguments/#spreading) an array of strings or content into a grid to populate its cells.
 * 
 * ```typ
 * #grid(
 *   columns: 5,
 *   gutter: 5pt,
 *   ..range(25).map(str)
 * )
 * ```
 * <img src="https://typst.app/assets/docs/qtEXI9WWslJNDT0wWvWAggAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Styling the grid_**
 * 
 * The grid and table elements work similarly. For a hands-on explanation, refer to the [Table Guide](https://typst.app/docs/guides/tables/#fills); for a quick overview, continue reading.
 * 
 * The grid's appearance can be customized through different parameters. These are the most important ones:
 * 
 * - [`align`](https://typst.app/docs/reference/layout/grid/#parameters-align) to change how cells are aligned
 * - [`inset`](https://typst.app/docs/reference/layout/grid/#parameters-inset) to optionally add internal padding to cells
 * - [`fill`](https://typst.app/docs/reference/layout/grid/#parameters-fill) to give cells a background
 * - [`stroke`](https://typst.app/docs/reference/layout/grid/#parameters-stroke) to optionally enable grid lines with a certain stroke
 * 
 * To meet different needs, there are various ways to set them.
 * 
 * If you need to override the above options for individual cells, you can use the [`grid.cell`](https://typst.app/docs/reference/layout/grid/#definitions-cell) element. Likewise, you can override individual grid lines with the [`grid.hline`](https://typst.app/docs/reference/layout/grid/#definitions-hline) and [`grid.vline`](https://typst.app/docs/reference/layout/grid/#definitions-vline) elements.
 * 
 * To configure an overall style for a grid, you may instead specify the option in any of the following fashions:
 * 
 * - As a single value that applies to all cells.
 * - As an array of values corresponding to each column. The array will be cycled if there are more columns than the array has items.
 * - As a function in the form of `(x, y) => value`. It receives the cell's column and row indices (both starting from zero) and should return the value to apply to that cell.
 * 
 * ```typ
 * #grid(
 *   columns: 5,
 * 
 *   // By a single value
 *   align: center,
 *   // By a single but more complicated value
 *   inset: (x: 2pt, y: 3pt),
 *   // By an array of values (cycling)
 *   fill: (rgb("#239dad50"), none),
 *   // By a function that returns a value
 *   stroke: (x, y) => if calc.rem(x + y, 3) == 0 { 0.5pt },
 * 
 *   ..range(5 * 3).map(n => numbering("A", n + 1))
 * )
 * ```
 * <img src="https://typst.app/assets/docs/W6AIy2JUBL4Q8rirw0EvqwAAAAAAAAAA.png" alt="Preview" />
 * 
 * On top of that, you may [apply styling rules](https://typst.app/docs/reference/styling/) to [`grid`](https://typst.app/docs/reference/layout/grid/) and [`grid.cell`](https://typst.app/docs/reference/layout/grid/#definitions-cell). Especially, the [`x`](https://typst.app/docs/reference/layout/grid/#definitions-cell-x) and [`y`](https://typst.app/docs/reference/layout/grid/#definitions-cell-y) fields of `grid.cell` can be used in a [`where`](https://typst.app/docs/reference/foundations/function/#definitions-where) selector, making it possible to style cells at specific columns or rows, or individual positions.
 * 
 * **_Stroke styling precedence_**
 * 
 * As explained above, there are three ways to set the stroke of a grid cell: through [`grid.cell`'s `stroke` field](https://typst.app/docs/reference/layout/grid/#definitions-cell-stroke), by using [`grid.hline`](https://typst.app/docs/reference/layout/grid/#definitions-hline) and [`grid.vline`](https://typst.app/docs/reference/layout/grid/#definitions-vline), or by setting the [`grid`'s `stroke` field](https://typst.app/docs/reference/layout/grid/#parameters-stroke). When multiple of these settings are present and conflict, the `hline` and `vline` settings take the highest precedence, followed by the `cell` settings, and finally the `grid` settings.
 * 
 * Furthermore, strokes of a repeated grid header or footer will take precedence over regular cell strokes.
 * 
 * **_Accessibility_**
 * 
 * Grids do not carry any special semantics. Assistive Technology (AT) does not offer the ability to navigate two-dimensionally by cell in grids. If you want to present tabular data, use the [`table`](https://typst.app/docs/reference/model/table/) element instead.
 * 
 * AT will read the grid cells in their semantic order. Usually, this is the order in which you passed them to the grid. However, if you manually positioned them using [`grid.cell`'s `x` and `y` arguments](https://typst.app/docs/reference/layout/grid/#definitions-cell-x), cells will be read row by row, from left to right (in left-to-right documents). A cell will be read when its position is first reached.
 */
@SerialName("grid")
data class TGrid(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/](https://typst.app/docs/reference/layout/grid/)
     * 
     * The contents of the grid cells, plus any extra grid lines specified with the [`grid.hline`](https://typst.app/docs/reference/layout/grid/#definitions-hline) and [`grid.vline`](https://typst.app/docs/reference/layout/grid/#definitions-vline) elements.
     * 
     * The cells are populated in row-major order.
     * 
     * Required, positional, variadic; Typst type: content
     */
    @all:Variadic @all:Positional val children: TArray<TContent>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/](https://typst.app/docs/reference/layout/grid/)
     * 
     * The column sizes.
     * 
     * Either specify a track size array or provide an integer to create a grid with that many `auto`-sized columns. Note that opposed to rows and gutters, providing a single track size will only ever create a single column.
     * 
     * See the [track size section](https://typst.app/docs/reference/layout/grid/#track-size) above for more details.
     * 
     * Settable; Typst type: auto|int|relative|fraction|array
     */
    @all:Settable val columns: ArrayOrSingle<Smart<TrackSize>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/](https://typst.app/docs/reference/layout/grid/)
     * 
     * The row sizes.
     * 
     * If there are more cells than fit the defined rows, the last row is repeated until there are no more cells.
     * 
     * See the [track size section](https://typst.app/docs/reference/layout/grid/#track-size) above for more details.
     * 
     * Settable; Typst type: auto|int|relative|fraction|array
     */
    @all:Settable val rows: ArrayOrSingle<Smart<TrackSize>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/](https://typst.app/docs/reference/layout/grid/)
     * 
     * The gaps between rows and columns. This is a shorthand to set [`column-gutter`](https://typst.app/docs/reference/layout/grid/#parameters-column-gutter) and [`row-gutter`](https://typst.app/docs/reference/layout/grid/#parameters-row-gutter) to the same value.
     * 
     * If there are more gutters than defined sizes, the last gutter is repeated.
     * 
     * See the [track size section](https://typst.app/docs/reference/layout/grid/#track-size) above for more details.
     * 
     * Typst type: auto|int|relative|fraction|array
     */
    @all:Settable val gutter: ArrayOrSingle<Smart<TrackSize>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/](https://typst.app/docs/reference/layout/grid/)
     * 
     * The gaps between columns.
     * 
     * Settable; Typst type: auto|int|relative|fraction|array
     */
    @all:Settable val columnGutter: ArrayOrSingle<Smart<TrackSize>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/](https://typst.app/docs/reference/layout/grid/)
     * 
     * The gaps between rows.
     * 
     * Settable; Typst type: auto|int|relative|fraction|array
     */
    @all:Settable val rowGutter: ArrayOrSingle<Smart<TrackSize>>?,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/](https://typst.app/docs/reference/layout/grid/)
     * 
     * How much to pad the cells' content.
     * 
     * To specify a uniform inset for all cells, you can use a single length for all sides, or a dictionary of lengths for individual sides. See the [box's documentation](https://typst.app/docs/reference/layout/box/#parameters-inset) for more details.
     * 
     * To specify varying inset for different cells, you can:
     * 
     * - use a single inset for all cells
     * - use an array of insets corresponding to each column
     * - use a function that maps a cell's position to its inset
     * 
     * See the [styling section](https://typst.app/docs/reference/layout/grid/#styling) above for more details.
     * 
     * In addition, you can find an example at the [`table.inset`](https://typst.app/docs/reference/model/table/#parameters-inset) parameter.
     * 
     * Settable; Typst type: relative|array|dictionary|function
     */
    @all:Settable val inset: Progression<Sides<TRelative>>?,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/](https://typst.app/docs/reference/layout/grid/)
     * 
     * How to align the cells' content.
     * 
     * If set to `auto`, the outer alignment is used.
     * 
     * You can specify the alignment in any of the following fashions:
     * 
     * - use a single alignment for all cells
     * - use an array of alignments corresponding to each column
     * - use a function that maps a cell's position to its alignment
     * 
     * See the [styling section](https://typst.app/docs/reference/layout/grid/#styling) above for details.
     * 
     * In addition, you can find an example at the [`table.align`](https://typst.app/docs/reference/model/table/#parameters-align) parameter.
     * 
     * Settable; Typst type: auto|array|alignment|function
     */
    @all:Settable val align: Progression<Smart<TAlignment>>?,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/](https://typst.app/docs/reference/layout/grid/)
     * 
     * How to fill the cells.
     * 
     * This can be:
     * 
     * - a single color for all cells
     * - an array of colors corresponding to each column
     * - a function that maps a cell's position to its color
     * 
     * Most notably, arrays and functions are useful for creating striped grids. See the [styling section](https://typst.app/docs/reference/layout/grid/#styling) above for more details.
     * 
     * Settable; Typst type: none|color|gradient|array|tiling|function
     */
    @all:Settable val fill: Progression<Option<TPaint>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/grid/](https://typst.app/docs/reference/layout/grid/)
     * 
     * How to [stroke](https://typst.app/docs/reference/visualize/stroke/) the cells.
     * 
     * Grids have no strokes by default, which can be changed by setting this option to the desired stroke.
     * 
     * If it is necessary to place lines which can cross spacing between cells produced by the [`gutter`](https://typst.app/docs/reference/layout/grid/#parameters-gutter) option, or to override the stroke between multiple specific cells, consider specifying one or more of [`grid.hline`](https://typst.app/docs/reference/layout/grid/#definitions-hline) and [`grid.vline`](https://typst.app/docs/reference/layout/grid/#definitions-vline) alongside your grid cells.
     * 
     * To specify the same stroke for all cells, you can use a single [stroke](https://typst.app/docs/reference/visualize/stroke/) for all sides, or a dictionary of [strokes](https://typst.app/docs/reference/visualize/stroke/) for individual sides. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-stroke) for more details.
     * 
     * To specify varying strokes for different cells, you can:
     * 
     * - use a single stroke for all cells
     * - use an array of strokes corresponding to each column
     * - use a function that maps a cell's position to its stroke
     * 
     * See the [styling section](https://typst.app/docs/reference/layout/grid/#styling) above for more details.
     * 
     * Settable; Typst type: none|length|color|gradient|array|stroke|tiling|dictionary|function
     */
    @all:Settable val stroke: Progression<Sides<Option<TStroke>>>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("grid")
    }
}


@SerialName("set-grid")
data class TSetGrid(
    override val internals: SetRuleInternals? = null,
    val columns: ArrayOrSingle<Smart<TrackSize>>? = null,
    val rows: ArrayOrSingle<Smart<TrackSize>>? = null,
    val gutter: ArrayOrSingle<Smart<TrackSize>>? = null,
    val columnGutter: ArrayOrSingle<Smart<TrackSize>>? = null,
    val rowGutter: ArrayOrSingle<Smart<TrackSize>>? = null,
    val inset: Progression<Sides<TRelative>>? = null,
    val align: Progression<Smart<TAlignment>>? = null,
    val fill: Progression<Option<TPaint>>? = null,
    val stroke: Progression<Option<TStroke>>? = null
) : TSetRule()
