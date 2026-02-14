package org.ldemetrios.tyko.model


//!https://typst.app/docs/reference/visualize/line/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/line/](https://typst.app/docs/reference/visualize/line/)
 * 
 * A line from one point to another.
 * 
 * **_Example_**
 * 
 * ```typ
 * #set page(height: 100pt)
 * 
 * #line(length: 100%)
 * #line(end: (50%, 50%))
 * #line(
 *   length: 4cm,
 *   stroke: 2pt + maroon,
 * )
 * ```
 * <img src="https://typst.app/assets/docs/IBdLCKW0h9kNWs6W_8DKAwAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("line")
data class TLine(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/line/](https://typst.app/docs/reference/visualize/line/)
     * 
     * The start point of the line.
     * 
     * Must be an array of exactly two relative lengths.
     * 
     * Settable; Typst type: array
     */
    @all:Settable val start: TPoint<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/line/](https://typst.app/docs/reference/visualize/line/)
     * 
     * The point where the line ends.
     * 
     * Settable; Typst type: none|array
     */
    @all:Settable val end: Option<TPoint<TRelative>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/line/](https://typst.app/docs/reference/visualize/line/)
     * 
     * The line's length. This is only respected if `end` is `none`.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val length: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/line/](https://typst.app/docs/reference/visualize/line/)
     * 
     * The angle at which the line points away from the origin. This is only respected if `end` is `none`.
     * 
     * Settable; Typst type: angle
     */
    @all:Settable val angle: TAngle? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/line/](https://typst.app/docs/reference/visualize/line/)
     * 
     * How to [stroke](https://typst.app/docs/reference/visualize/stroke/) the line.
     * 
     * Settable; Typst type: length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: TStroke? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("line")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TLine]
 */
@SerialName("set-line")
data class TSetLine(
    override val internals: SetRuleInternals? = null,
    val start: TPoint<TRelative>? = null,
    val end: Option<TPoint<TRelative>>? = null,
    val length: TRelative? = null,
    val angle: TAngle? = null,
    val stroke: TStroke? = null
) : TSetRule()
