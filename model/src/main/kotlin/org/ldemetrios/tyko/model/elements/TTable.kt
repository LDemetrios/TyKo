package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/table/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/table/](https://typst.app/docs/reference/model/table/)
 * 
 * A table of items.
 * 
 * Tables are used to arrange content in cells. Cells can contain arbitrary content, including multiple paragraphs and are specified in row-major order. For a hands-on explanation of all the ways you can use and customize tables in Typst, check out the [Table Guide](https://typst.app/docs/guides/tables/).
 * 
 * Because tables are just grids with different defaults for some cell properties (notably `stroke` and `inset`), refer to the [grid documentation](https://typst.app/docs/reference/layout/grid/#track-size) for more information on how to size the table tracks and specify the cell appearance properties.
 * 
 * If you are unsure whether you should be using a table or a grid, consider whether the content you are arranging semantically belongs together as a set of related data points or similar or whether you are just want to enhance your presentation by arranging unrelated content in a grid. In the former case, a table is the right choice, while in the latter case, a grid is more appropriate. Furthermore, Assistive Technology (AT) like screen readers will announce content in a `table` as tabular while a grid's content will be announced no different than multiple content blocks in the document flow. AT users will be able to navigate tables two-dimensionally by cell.
 * 
 * Note that, to override a particular cell's properties or apply show rules on table cells, you can use the [`table.cell`](https://typst.app/docs/reference/model/table/#definitions-cell) element. See its documentation for more information.
 * 
 * Although the `table` and the `grid` share most properties, set and show rules on one of them do not affect the other. Locating most of your styling in set and show rules is recommended, as it keeps the table's actual usages clean and easy to read. It also allows you to easily change the appearance of all tables in one place.
 * 
 * To give a table a caption and make it [referenceable](https://typst.app/docs/reference/model/ref/), put it into a [figure](https://typst.app/docs/reference/model/figure/).
 * 
 * **_Example_**
 * 
 * The example below demonstrates some of the most common table options.
 * 
 * ```typ
 * #table(
 *   columns: (1fr, auto, auto),
 *   inset: 10pt,
 *   align: horizon,
 *   table.header(
 *     [], [*Volume*], [*Parameters*],
 *   ),
 *   image("cylinder.svg"),
 *   $ pi h (D^2 - d^2) / 4 $,
 *   [
 *     $h$: height \
 *     $D$: outer radius \
 *     $d$: inner radius
 *   ],
 *   image("tetrahedron.svg"),
 *   $ sqrt(2) / 12 a^3 $,
 *   [$a$: edge length]
 * )
 * ```
 * <img src="https://typst.app/assets/docs/KSzjBsOqtudzwvK6Zvp9uwAAAAAAAAAA.png" alt="Preview" />
 * 
 * Much like with grids, you can use [`table.cell`](https://typst.app/docs/reference/model/table/#definitions-cell) to customize the appearance and the position of each cell.
 * 
 * ```typ
 * #set table(
 *   stroke: none,
 *   gutter: 0.2em,
 *   fill: (x, y) =>
 *     if x == 0 or y == 0 { gray },
 *   inset: (right: 1.5em),
 * )
 * 
 * #show table.cell: it => {
 *   if it.x == 0 or it.y == 0 {
 *     set text(white)
 *     strong(it)
 *   } else if it.body == [] {
 *     // Replace empty cells with 'N/A'
 *     pad(..it.inset)[_N/A_]
 *   } else {
 *     it
 *   }
 * }
 * 
 * #let a = table.cell(
 *   fill: green.lighten(60%),
 * )[A]
 * #let b = table.cell(
 *   fill: aqua.lighten(60%),
 * )[B]
 * 
 * #table(
 *   columns: 4,
 *   [], [Exam 1], [Exam 2], [Exam 3],
 * 
 *   [John], [], a, [],
 *   [Mary], [], a, a,
 *   [Robert], b, a, b,
 * )
 * ```
 * <img src="https://typst.app/assets/docs/D_wYQ9Nqm8ZPq6ssgJwiZQAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Accessibility_**
 * 
 * Tables are challenging to consume for users of Assistive Technology (AT). To make the life of AT users easier, we strongly recommend that you use [`table.header`](https://typst.app/docs/reference/model/table/#definitions-header) and [`table.footer`](https://typst.app/docs/reference/model/table/#definitions-footer) to mark the header and footer sections of your table. This will allow AT to announce the column labels for each cell.
 * 
 * Because navigating a table by cell is more cumbersome than reading it visually, you should consider making the core information in your table available as text as well. You can do this by wrapping your table in a [figure](https://typst.app/docs/reference/model/figure/) and using its caption to summarize the table's content.
 */
@SerialName("table")
data class TTable(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/](https://typst.app/docs/reference/model/table/)
     * 
     * The contents of the table cells, plus any extra table lines specified with the [`table.hline`](https://typst.app/docs/reference/model/table/#definitions-hline) and [`table.vline`](https://typst.app/docs/reference/model/table/#definitions-vline) elements.
     * 
     * Required, positional, variadic; Typst type: content
     */
    @all:Variadic @all:Positional val children: TArray<TContent>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/](https://typst.app/docs/reference/model/table/)
     * 
     * The column sizes. See the [grid documentation](https://typst.app/docs/reference/layout/grid/#track-size) for more information on track sizing.
     * 
     * Settable; Typst type: auto|int|relative|fraction|array
     */
    @all:Settable val columns: ArrayOrSingle<Smart<TrackSize>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/](https://typst.app/docs/reference/model/table/)
     * 
     * The row sizes. See the [grid documentation](https://typst.app/docs/reference/layout/grid/#track-size) for more information on track sizing.
     * 
     * Settable; Typst type: auto|int|relative|fraction|array
     */
    @all:Settable val rows: ArrayOrSingle<Smart<TrackSize>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/](https://typst.app/docs/reference/model/table/)
     * 
     * The gaps between rows and columns. This is a shorthand for setting `column-gutter` and `row-gutter` to the same value. See the [grid documentation](https://typst.app/docs/reference/layout/grid/#parameters-gutter) for more information on gutters.
     * 
     * Typst type: auto|int|relative|fraction|array
     */
    @all:Settable val gutter: ArrayOrSingle<Smart<TrackSize>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/](https://typst.app/docs/reference/model/table/)
     * 
     * The gaps between columns. Takes precedence over `gutter`. See the [grid documentation](https://typst.app/docs/reference/layout/grid/#parameters-gutter) for more information on gutters.
     * 
     * Settable; Typst type: auto|int|relative|fraction|array
     */
    @all:Settable val columnGutter: ArrayOrSingle<Smart<TrackSize>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/](https://typst.app/docs/reference/model/table/)
     * 
     * The gaps between rows. Takes precedence over `gutter`. See the [grid documentation](https://typst.app/docs/reference/layout/grid/#parameters-gutter) for more information on gutters.
     * 
     * Settable; Typst type: auto|int|relative|fraction|array
     */
    @all:Settable val rowGutter: ArrayOrSingle<Smart<TrackSize>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/](https://typst.app/docs/reference/model/table/)
     * 
     * How much to pad the cells' content.
     * 
     * To specify the same inset for all cells, use a single length for all sides, or a dictionary of lengths for individual sides. See the [box's documentation](https://typst.app/docs/reference/layout/box/#parameters-inset) for more details.
     * 
     * To specify a varying inset for different cells, you can:
     * 
     * - use a single, uniform inset for all cells
     * - use an array of insets for each column
     * - use a function that maps a cell's X/Y position (both starting from zero) to its inset
     * 
     * See the [grid documentation](https://typst.app/docs/reference/layout/grid/#styling) for more details.
     * 
     * Settable; Typst type: relative|array|dictionary|function
     */
    @all:Settable val inset: Progression<Sides<TRelative>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/](https://typst.app/docs/reference/model/table/)
     * 
     * How to align the cells' content.
     * 
     * If set to `auto`, the outer alignment is used.
     * 
     * You can specify the alignment in any of the following fashions:
     * 
     * - use a single alignment for all cells
     * - use an array of alignments corresponding to each column
     * - use a function that maps a cell's X/Y position (both starting from zero) to its alignment
     * 
     * See the [Table Guide](https://typst.app/docs/guides/tables/#alignment) for details.
     * 
     * Settable; Typst type: auto|array|alignment|function
     */
    @all:Settable val align: Progression<Smart<TAlignment>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/](https://typst.app/docs/reference/model/table/)
     * 
     * How to fill the cells.
     * 
     * This can be:
     * 
     * - a single fill for all cells
     * - an array of fill corresponding to each column
     * - a function that maps a cell's position to its fill
     * 
     * Most notably, arrays and functions are useful for creating striped tables. See the [Table Guide](https://typst.app/docs/guides/tables/#fills) for more details.
     * 
     * Settable; Typst type: none|color|gradient|array|tiling|function
     */
    @all:Settable val fill: Progression<Option<TPaint>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/](https://typst.app/docs/reference/model/table/)
     * 
     * How to [stroke](https://typst.app/docs/reference/visualize/stroke/) the cells.
     * 
     * Strokes can be disabled by setting this to `none`.
     * 
     * If it is necessary to place lines which can cross spacing between cells produced by the [`gutter`](https://typst.app/docs/reference/model/table/#parameters-gutter) option, or to override the stroke between multiple specific cells, consider specifying one or more of [`table.hline`](https://typst.app/docs/reference/model/table/#definitions-hline) and [`table.vline`](https://typst.app/docs/reference/model/table/#definitions-vline) alongside your table cells.
     * 
     * To specify the same stroke for all cells, use a single [stroke](https://typst.app/docs/reference/visualize/stroke/) for all sides, or a dictionary of [strokes](https://typst.app/docs/reference/visualize/stroke/) for individual sides. See the [rectangle's documentation](https://typst.app/docs/reference/visualize/rect/#parameters-stroke) for more details.
     * 
     * To specify varying strokes for different cells, you can:
     * 
     * - use a single stroke for all cells
     * - use an array of strokes corresponding to each column
     * - use a function that maps a cell's position to its stroke
     * 
     * See the [Table Guide](https://typst.app/docs/guides/tables/#strokes) for more details.
     * 
     * Settable; Typst type: none|length|color|gradient|array|stroke|tiling|dictionary|function
     */
    @all:Settable val stroke: Progression<Option<TStroke>>? = null,
    override val label: TLabel? = null
) : TSelectableContent<TTable>() {
    override fun elem(): TLocatableElement<TTable> = ELEM

    companion object {
        val ELEM = TLocatableElement<TTable>("table")
    }
}


@SerialName("set-table")
data class TSetTable(
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
