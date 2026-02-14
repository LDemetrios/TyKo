package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/footnote/#definitions-entry
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/footnote/#definitions-entry](https://typst.app/docs/reference/model/footnote/#definitions-entry)
 * 
 * An entry in a footnote list.
 * 
 * This function is not intended to be called directly. Instead, it is used in set and show rules to customize footnote listings.
 * 
 * *Note:* Footnote entry properties must be uniform across each page run (a page run is a sequence of pages without an explicit pagebreak in between). For this reason, set and show rules for footnote entries should be defined before any page content, typically at the very start of the document.
 */
@SerialName("footnote.entry")
data class TFootnoteEntry(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/footnote/#definitions-entry](https://typst.app/docs/reference/model/footnote/#definitions-entry)
     * 
     * The footnote for this entry. Its location can be used to determine the footnote counter state.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val note: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/footnote/#definitions-entry](https://typst.app/docs/reference/model/footnote/#definitions-entry)
     * 
     * The separator between the document body and the footnote listing.
     * 
     * Settable; Typst type: content
     */
    @all:Settable val separator: TContent? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/footnote/#definitions-entry](https://typst.app/docs/reference/model/footnote/#definitions-entry)
     * 
     * The amount of clearance between the document body and the separator.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val clearance: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/footnote/#definitions-entry](https://typst.app/docs/reference/model/footnote/#definitions-entry)
     * 
     * The gap between footnote entries.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val gap: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/footnote/#definitions-entry](https://typst.app/docs/reference/model/footnote/#definitions-entry)
     * 
     * The indent of each footnote entry.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val indent: TLength? = null,
    override val label: TLabel? = null
) : TSelectableContent<TFootnoteEntry>() {
    override fun elem(): TLocatableElement<TFootnoteEntry> = ELEM

    companion object {
        val ELEM = TLocatableElement<TFootnoteEntry>("footnote.entry")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TFootnoteEntry]
 */
@SerialName("set-footnote.entry")
data class TSetFootnoteEntry(
    override val internals: SetRuleInternals? = null,
    val separator: TContent? = null,
    val clearance: TLength? = null,
    val gap: TLength? = null,
    val indent: TLength? = null
) : TSetRule()
