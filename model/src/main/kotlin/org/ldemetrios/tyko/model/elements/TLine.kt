package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/visualize/line/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
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
     * The start point of the line.
     * 
     * Must be an array of exactly two relative lengths.
     * 
     * Settable; Typst type: array
     */
    @all:Settable val start: Point<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The point where the line ends.
     * 
     * Settable; Typst type: none|array
     */
    @all:Settable val end: Option<Point<TRelative>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The line's length. This is only respected if `end` is `none`.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val length: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The angle at which the line points away from the origin. This is only respected if `end` is `none`.
     * 
     * Settable; Typst type: angle
     */
    @all:Settable val angle: TAngle? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
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


@SerialName("set-line")
data class TSetLine(
    override val internals: SetRuleInternals? = null,
    val start: Point<TRelative>? = null,
    val end: Option<Point<TRelative>>? = null,
    val length: TRelative? = null,
    val angle: TAngle? = null,
    val stroke: TStroke? = null
) : TSetRule()
