package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/visualize/polygon/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/polygon/](https://typst.app/docs/reference/visualize/polygon/)
 * 
 * A closed polygon.
 * 
 * The polygon is defined by its corner points and is closed automatically.
 * 
 * **_Example_**
 * 
 * ```typ
 * #polygon(
 *   fill: blue.lighten(80%),
 *   stroke: blue,
 *   (20%, 0pt),
 *   (60%, 0pt),
 *   (80%, 2cm),
 *   (0%,  2cm),
 * )
 * ```
 * <img src="https://typst.app/assets/docs/TuzATomarVg-0NmUVu3QFAAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("polygon")
data class TPolygon(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/polygon/](https://typst.app/docs/reference/visualize/polygon/)
     * 
     * The vertices of the polygon. Each point is specified as an array of two [relative lengths](https://typst.app/docs/reference/layout/relative/).
     * 
     * Required, positional, variadic; Typst type: array
     */
    @all:Variadic @all:Positional val vertices: TArray<Point<TRelative>>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/polygon/](https://typst.app/docs/reference/visualize/polygon/)
     * 
     * How to fill the polygon.
     * 
     * When setting a fill, the default stroke disappears. To create a rectangle with both fill and stroke, you have to configure both.
     * 
     * Settable; Typst type: none|color|gradient|tiling
     */
    @all:Settable val fill: Option<TPaint>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/polygon/](https://typst.app/docs/reference/visualize/polygon/)
     * 
     * The drawing rule used to fill the polygon.
     * 
     * See the [curve documentation](https://typst.app/docs/reference/visualize/curve/#parameters-fill-rule) for an example.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"non-zero"` | Specifies that "inside" is computed by a non-zero sum of signed edge crossings. |
     * | `"even-odd"` | Specifies that "inside" is computed by an odd number of edge crossings. |
     * 
     * Settable; Typst type: str
     */
    @all:Settable val fillRule: TStr? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/polygon/](https://typst.app/docs/reference/visualize/polygon/)
     * 
     * How to [stroke](https://typst.app/docs/reference/visualize/stroke/) the polygon.
     * 
     * Can be set to `none` to disable the stroke or to `auto` for a stroke of `1pt` black if and only if no fill is given.
     * 
     * Settable; Typst type: none|auto|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Smart<Option<TStroke>>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("polygon")
    }
}


@SerialName("set-polygon")
data class TSetPolygon(
    override val internals: SetRuleInternals? = null,
    val fill: Option<TPaint>? = null,
    val fillRule: TStr? = null,
    val stroke: Smart<Option<TStroke>>? = null
) : TSetRule()
