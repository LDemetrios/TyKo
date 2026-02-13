package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/outline/#definitions-entry
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/outline/#definitions-entry](https://typst.app/docs/reference/model/outline/#definitions-entry)
 * 
 * Represents an entry line in an outline.
 * 
 * With show-set and show rules on outline entries, you can richly customize the outline's appearance. See the [section on styling the outline](https://typst.app/docs/reference/model/outline/#styling-the-outline) for details.
 */
@SerialName("outline.entry")
data class TOutlineEntry(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/outline/#definitions-entry](https://typst.app/docs/reference/model/outline/#definitions-entry)
     * 
     * The nesting level of this outline entry. Starts at `1` for top-level entries.
     * 
     * Required, positional; Typst type: int
     */
    @all:Positional val level: TInt,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/outline/#definitions-entry](https://typst.app/docs/reference/model/outline/#definitions-entry)
     * 
     * The element this entry refers to. Its location will be available through the [`location`](https://typst.app/docs/reference/foundations/content/#definitions-location) method on the content and can be [linked](https://typst.app/docs/reference/model/link/) to.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val element: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/outline/#definitions-entry](https://typst.app/docs/reference/model/outline/#definitions-entry)
     * 
     * Content to fill the space between the title and the page number. Can be set to `none` to disable filling.
     * 
     * The `fill` will be placed into a fractionally sized box that spans the space between the entry's body and the page number. When using show rules to override outline entries, it is thus recommended to wrap the fill in a [`box`](https://typst.app/docs/reference/layout/box/) with fractional width, i.e. `box(width: 1fr, it.fill)`.
     * 
     * When using [`repeat`](https://typst.app/docs/reference/layout/repeat/), the [`gap`](https://typst.app/docs/reference/layout/repeat/#parameters-gap) property can be useful to tweak the visual weight of the fill.
     * 
     * Settable; Typst type: none|content
     */
    @all:Settable val fill: Option<TContent>? = null,
    override val label: TLabel? = null
) : TSelectableContent<TOutlineEntry>() {
    override fun elem(): TLocatableElement<TOutlineEntry> = ELEM

    companion object {
        val ELEM = TLocatableElement<TOutlineEntry>("outline.entry")
    }
}


@SerialName("set-outline.entry")
data class TSetOutlineEntry(
    override val internals: SetRuleInternals? = null,
    val fill: Option<TContent>? = null
) : TSetRule()
