package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.Numbering


//!https://typst.app/docs/reference/model/figure/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/figure/](https://typst.app/docs/reference/model/figure/)
 * 
 * A figure with an optional caption.
 * 
 * Automatically detects its kind to select the correct counting track. For example, figures containing images will be numbered separately from figures containing tables.
 * 
 * **_Examples_**
 * 
 * The example below shows a basic figure with an image:
 * 
 * ```typ
 * @glacier shows a glacier. Glaciers
 * are complex systems.
 * 
 * #figure(
 *   image("glacier.jpg", width: 80%),
 *   caption: [A curious figure.],
 * ) <glacier>
 * ```
 * <img src="https://typst.app/assets/docs/udw8J1zW8CDfoYB1XTzdLgAAAAAAAAAA.png" alt="Preview" />
 * 
 * You can also insert [tables](https://typst.app/docs/reference/model/table/) into figures to give them a caption. The figure will detect this and automatically use a separate counter.
 * 
 * ```typ
 * #figure(
 *   table(
 *     columns: 4,
 *     [t], [1], [2], [3],
 *     [y], [0.3s], [0.4s], [0.8s],
 *   ),
 *   caption: [Timing results],
 * )
 * ```
 * <img src="https://typst.app/assets/docs/_RaOJik9O5UoQO8Eq6OM9gAAAAAAAAAA.png" alt="Preview" />
 * 
 * This behaviour can be overridden by explicitly specifying the figure's `kind`. All figures of the same kind share a common counter.
 * 
 * **_Figure behaviour_**
 * 
 * By default, figures are placed within the flow of content. To make them float to the top or bottom of the page, you can use the [`placement`](https://typst.app/docs/reference/model/figure/#parameters-placement) argument.
 * 
 * If your figure is too large and its contents are breakable across pages (e.g. if it contains a large table), then you can make the figure itself breakable across pages as well with this show rule:
 * 
 * `#show figure: set block(breakable: true)`
 * 
 * See the [block](https://typst.app/docs/reference/layout/block/#parameters-breakable) documentation for more information about breakable and non-breakable blocks.
 * 
 * **_Caption customization_**
 * 
 * You can modify the appearance of the figure's caption with its associated [`caption`](https://typst.app/docs/reference/model/figure/#definitions-caption) function. In the example below, we emphasize all captions:
 * 
 * ```typ
 * #show figure.caption: emph
 * 
 * #figure(
 *   rect[Hello],
 *   caption: [I am emphasized!],
 * )
 * ```
 * <img src="https://typst.app/assets/docs/_XYhSBTt1dmjYR9A4n_aCgAAAAAAAAAA.png" alt="Preview" />
 * 
 * By using a [`where`](https://typst.app/docs/reference/foundations/function/#definitions-where) selector, we can scope such rules to specific kinds of figures. For example, to position the caption above tables, but keep it below for all other kinds of figures, we could write the following show-set rule:
 * 
 * ```typ
 * #show figure.where(
 *   kind: table
 * ): set figure.caption(position: top)
 * 
 * #figure(
 *   table(columns: 2)[A][B][C][D],
 *   caption: [I'm up here],
 * )
 * ```
 * <img src="https://typst.app/assets/docs/5FXY-vQKID4Q1FYsR4Ix9AAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Accessibility_**
 * 
 * You can use the [`alt`](https://typst.app/docs/reference/model/figure/#parameters-alt) parameter to provide an [alternative description](https://typst.app/docs/guides/accessibility/#textual-representations) of the figure for screen readers and other Assistive Technology (AT). Refer to [its documentation](https://typst.app/docs/reference/model/figure/#parameters-alt) to learn more.
 * 
 * You can use figures to add alternative descriptions to paths, shapes, or visualizations that do not have their own `alt` parameter. If your graphic is purely decorative and does not have a semantic meaning, consider wrapping it in [`pdf.artifact`](https://typst.app/docs/reference/pdf/artifact/) instead, which will hide it from AT when exporting to PDF.
 * 
 * AT will always read the figure at the point where it appears in the document, regardless of its [`placement`](https://typst.app/docs/reference/model/figure/#parameters-placement). Put its markup where it would make the most sense in the reading order.
 */
@SerialName("figure")
data class TFigure(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/figure/](https://typst.app/docs/reference/model/figure/)
     * 
     * The content of the figure. Often, an [image](https://typst.app/docs/reference/visualize/image/).
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/figure/](https://typst.app/docs/reference/model/figure/)
     * 
     * An alternative description of the figure.
     * 
     * When you add an alternative description, AT will read both it and the caption (if any). However, the content of the figure itself will be skipped.
     * 
     * When the body of your figure is an [image](https://typst.app/docs/reference/visualize/image/) with its own `alt` text set, this parameter should not be used on the figure element. Likewise, do not use this parameter when the figure contains a table, code, or other content that is already accessible. In such cases, the content of the figure will be read by AT, and adding an alternative description would lead to a loss of information.
     * 
     * You can learn how to write good alternative descriptions in the [Accessibility Guide](https://typst.app/docs/guides/accessibility/#textual-representations).
     * 
     * Settable; Typst type: none|str
     */
    @all:Settable val alt: Option<TStr>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/figure/](https://typst.app/docs/reference/model/figure/)
     * 
     * The figure's placement on the page.
     * 
     * - `none`: The figure stays in-flow exactly where it was specified like other content.
     * - `auto`: The figure picks `top` or `bottom` depending on which is closer.
     * - `top`: The figure floats to the top of the page.
     * - `bottom`: The figure floats to the bottom of the page.
     * 
     * The gap between the main flow content and the floating figure is controlled by the [`clearance`](https://typst.app/docs/reference/layout/place/#parameters-clearance) argument on the `place` function.
     * 
     * Settable; Typst type: none|auto|alignment
     */
    @all:Settable val placement: Smart<Option<TAlignment>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/figure/](https://typst.app/docs/reference/model/figure/)
     * 
     * Relative to which containing scope the figure is placed.
     * 
     * Set this to `"parent"` to create a full-width figure in a two-column document.
     * 
     * Has no effect if `placement` is `none`.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"column"` | Place into the current column. |
     * | `"parent"` | Place relative to the parent, letting the content span over all columns. |
     * 
     * Settable; Typst type: str
     */
    @all:Settable val scope: TFigureScope? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/figure/](https://typst.app/docs/reference/model/figure/)
     * 
     * The figure's caption.
     * 
     * Settable; Typst type: none|content
     */
    @all:Settable val caption: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/figure/](https://typst.app/docs/reference/model/figure/)
     * 
     * The kind of figure this is.
     * 
     * All figures of the same kind share a common counter.
     * 
     * If set to `auto`, the figure will try to automatically determine its kind based on the type of its body. Automatically detected kinds are [tables](https://typst.app/docs/reference/model/table/) and [code](https://typst.app/docs/reference/text/raw/). In other cases, the inferred kind is that of an [image](https://typst.app/docs/reference/visualize/image/).
     * 
     * Setting this to something other than `auto` will override the automatic detection. This can be useful if
     * 
     * - you wish to create a custom figure type that is not an [image](https://typst.app/docs/reference/visualize/image/), a [table](https://typst.app/docs/reference/model/table/) or [code](https://typst.app/docs/reference/text/raw/),
     * - you want to force the figure to use a specific counter regardless of its content.
     * 
     * You can set the kind to be an element function or a string. If you set it to an element function other than [`table`](https://typst.app/docs/reference/model/table/), [`raw`](https://typst.app/docs/reference/text/raw/), or [`image`](https://typst.app/docs/reference/visualize/image/), you will need to manually specify the figure's supplement.
     * 
     * If you want to modify a counter to skip a number or reset the counter, you can access the [counter](https://typst.app/docs/reference/introspection/counter/) of each kind of figure with a [`where`](https://typst.app/docs/reference/foundations/function/#definitions-where) selector:
     * 
     * - For [tables](https://typst.app/docs/reference/model/table/): `counter(figure.where(kind: table))`
     * - For [images](https://typst.app/docs/reference/visualize/image/): `counter(figure.where(kind: image))`
     * - For a custom kind: `counter(figure.where(kind: kind))`
     * 
     * To conveniently use the correct counter in a show rule, you can access the `counter` field. There is an example of this in the documentation [of the `figure.caption` element's `body` field](https://typst.app/docs/reference/model/figure/#definitions-caption-body).
     * 
     * Settable; Typst type: auto|str|function
     */
    @all:Settable val kind: Smart<TFigureKind>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/figure/](https://typst.app/docs/reference/model/figure/)
     * 
     * The figure's supplement.
     * 
     * If set to `auto`, the figure will try to automatically determine the correct supplement based on the `kind` and the active [text language](https://typst.app/docs/reference/text/text/#parameters-lang). If you are using a custom figure type, you will need to manually specify the supplement.
     * 
     * If a function is specified, it is passed the first descendant of the specified `kind` (typically, the figure's body) and should return content.
     * 
     * Settable; Typst type: none|auto|content|function
     */
    @all:Settable val supplement: Smart<Option<Computable<TContent>>>?,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/figure/](https://typst.app/docs/reference/model/figure/)
     * 
     * How to number the figure. Accepts a [numbering pattern or function](https://typst.app/docs/reference/model/numbering/) taking a single number.
     * 
     * Settable; Typst type: none|str|function
     */
    @all:Settable val numbering: Option<Numbering>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/figure/](https://typst.app/docs/reference/model/figure/)
     * 
     * The vertical gap between the body and caption.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val gap: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/figure/](https://typst.app/docs/reference/model/figure/)
     * 
     * Whether the figure should appear in an [`outline`](https://typst.app/docs/reference/model/outline/) of figures.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val outlined: TBool? = null,
    override val label: TLabel? = null
) : TSelectableContent<TFigure>() {
    override fun elem(): TLocatableElement<TFigure> = ELEM

    companion object {
        val ELEM = TLocatableElement<TFigure>("figure")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TFigure]
 */
@SerialName("set-figure")
data class TSetFigure(
    override val internals: SetRuleInternals? = null,
    val alt: Option<TStr>? = null,
    val placement: Smart<Option<TAlignment>>? = null,
    val scope: TFigureScope? = null,
    val caption: Option<TContent>? = null,
    val kind: Smart<TFigureKind>? = null,
    val supplement: Smart<Option<Computable<TContent>>>? = null,
    val numbering: Option<Numbering>? = null,
    val gap: TLength? = null,
    val outlined: TBool? = null
) : TSetRule()
