package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/table/#definitions-footer
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-footer](https://typst.app/docs/reference/model/table/#definitions-footer)
 * 
 * A repeatable table footer.
 * 
 * Just like the [`table.header`](https://typst.app/docs/reference/model/table/#definitions-header) element, the footer can repeat itself on every page of the table. This is useful for improving legibility by adding the column labels in both the header and footer of a large table, totals, or other information that should be visible on every page.
 * 
 * No other table cells may be placed after the footer.
 */
@SerialName("table.footer")
data class TTableFooter(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-footer](https://typst.app/docs/reference/model/table/#definitions-footer)
     * 
     * The cells and lines within the footer.
     * 
     * Required, positional, variadic; Typst type: content
     */
    @all:Variadic @all:Positional  val children: TArray<TContent>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-footer](https://typst.app/docs/reference/model/table/#definitions-footer)
     * 
     * Whether this footer should be repeated across pages.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val repeat: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("table.footer")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TTableFooter]
 */
@SerialName("set-table.footer")
data class TSetTableFooter(
    override val internals: SetRuleInternals? = null,
    val repeat: TBool? = null
) : TSetRule()
