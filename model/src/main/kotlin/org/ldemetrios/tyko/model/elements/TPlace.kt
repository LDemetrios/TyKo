package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/place/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Places content relatively to its parent container.
 * 
 * Placed content can be either overlaid (the default) or floating. Overlaid content is aligned with the parent container according to the given [`alignment`](https://typst.app/docs/reference/layout/place/#parameters-alignment), and shown over any other content added so far in the container. Floating content is placed at the top or bottom of the container, displacing other content down or up respectively. In both cases, the content position can be adjusted with [`dx`](https://typst.app/docs/reference/layout/place/#parameters-dx) and [`dy`](https://typst.app/docs/reference/layout/place/#parameters-dy) offsets without affecting the layout.
 * 
 * The parent can be any container such as a [`block`](https://typst.app/docs/reference/layout/block/), [`box`](https://typst.app/docs/reference/layout/box/), [`rect`](https://typst.app/docs/reference/visualize/rect/), etc. A top level `place` call will place content directly in the text area of the current page. This can be used for absolute positioning on the page: with a `top + left`[`alignment`](https://typst.app/docs/reference/layout/place/#parameters-alignment), the offsets `dx` and `dy` will set the position of the element's top left corner relatively to the top left corner of the text area. For absolute positioning on the full page including margins, you can use `place` in [`page.foreground`](https://typst.app/docs/reference/layout/page/#parameters-foreground) or [`page.background`](https://typst.app/docs/reference/layout/page/#parameters-background).
 * 
 * **_Examples_**
 * 
 * ```typ
 * #set page(height: 120pt)
 * Hello, world!
 * 
 * #rect(
 *   width: 100%,
 *   height: 2cm,
 *   place(horizon + right, square()),
 * )
 * 
 * #place(
 *   top + left,
 *   dx: -5pt,
 *   square(size: 5pt, fill: red),
 * )
 * ```
 * <img src="https://typst.app/assets/docs/b3Ue37sNl2HDpslyo5trfgAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Effect on the position of other elements_**
 * 
 * Overlaid elements don't take space in the flow of content, but a `place` call inserts an invisible block-level element in the flow. This can affect the layout by breaking the current paragraph. To avoid this, you can wrap the `place` call in a [`box`](https://typst.app/docs/reference/layout/box/) when the call is made in the middle of a paragraph. The alignment and offsets will then be relative to this zero-size box. To make sure it doesn't interfere with spacing, the box should be attached to a word using a word joiner.
 * 
 * For example, the following defines a function for attaching an annotation to the following word:
 * 
 * ```typ
 * #let annotate(..args) = {
 *   box(place(..args))
 *   sym.wj
 *   h(0pt, weak: true)
 * }
 * 
 * A placed #annotate(square(), dy: 2pt)
 * square in my text.
 * ```
 * <img src="https://typst.app/assets/docs/QIJqPsAAp5jqe-EB4bZF1gAAAAAAAAAA.png" alt="Preview" />
 * 
 * The zero-width weak spacing serves to discard spaces between the function call and the next word.
 * 
 * **_Accessibility_**
 * 
 * Assistive Technology (AT) will always read the placed element at the point where it logically appears in the document, regardless of where this function physically moved it. Put its markup where it would make the most sense in the reading order.
 */
@SerialName("place")
data class TPlace(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Relative to which position in the parent container to place the content.
     * 
     * - If `float` is `false`, then this can be any alignment other than `auto`.
     * - If `float` is `true`, then this must be `auto`, `top`, or `bottom`.
     * 
     * When `float` is `false` and no vertical alignment is specified, the content is placed at the current position on the vertical axis.
     * 
     * Positional, settable; Typst type: auto|alignment
     */
    @all:Settable @all:Positional val alignment: Smart<TAlignment>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The content to place.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val body: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Relative to which containing scope something is placed.
     * 
     * The parent scope is primarily used with figures and, for this reason, the figure function has a mirrored [`scope` parameter](https://typst.app/docs/reference/model/figure/#parameters-scope). Nonetheless, it can also be more generally useful to break out of the columns. A typical example would be to [create a single-column title section](https://typst.app/docs/guides/page-setup/#columns) in a two-column document.
     * 
     * Note that parent-scoped placement is currently only supported if `float` is `true`. This may change in the future.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"column"` | Place into the current column. |
     * | `"parent"` | Place relative to the parent, letting the content span over all columns. |
     * 
     * Settable; Typst type: str
     */
    @all:Settable val scope: TStr? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether the placed element has floating layout.
     * 
     * Floating elements are positioned at the top or bottom of the parent container, displacing in-flow content. They are always placed in the in-flow order relative to each other, as well as before any content following a later [`place.flush`](https://typst.app/docs/reference/layout/place/#definitions-flush) element.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val float: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The spacing between the placed element and other elements in a floating layout.
     * 
     * Has no effect if `float` is `false`.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val clearance: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The horizontal displacement of the placed content.
     * 
     * This does not affect the layout of in-flow content. In other words, the placed content is treated as if it were wrapped in a [`move`](https://typst.app/docs/reference/layout/move/) element.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val dx: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The vertical displacement of the placed content.
     * 
     * This does not affect the layout of in-flow content. In other words, the placed content is treated as if it were wrapped in a [`move`](https://typst.app/docs/reference/layout/move/) element.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val dy: TRelative? = null,
    override val label: TLabel? = null
) : TSelectableContent<TPlace>() {
    override fun elem(): TLocatableElement<TPlace> = ELEM

    companion object {
        val ELEM = TLocatableElement<TPlace>("place")
    }
}


@SerialName("set-place")
data class TSetPlace(
    override val internals: SetRuleInternals? = null,
    @all:Positional val alignment: Smart<TAlignment>? = null,
    val scope: TStr? = null,
    val float: TBool? = null,
    val clearance: TLength? = null,
    val dx: TRelative? = null,
    val dy: TRelative? = null
) : TSetRule()
