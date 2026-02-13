package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/table/#definitions-header
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-header](https://typst.app/docs/reference/model/table/#definitions-header)
 * 
 * A repeatable table header.
 * 
 * You should wrap your tables' heading rows in this function even if you do not plan to wrap your table across pages because Typst uses this function to attach accessibility metadata to tables and ensure [Universal Access](https://typst.app/docs/guides/accessibility/#basics) to your document.
 * 
 * You can use the `repeat` parameter to control whether your table's header will be repeated across pages.
 * 
 * Currently, this function is unsuitable for creating a header column or single header cells. Either use regular cells, or, if you are exporting a PDF, you can also use the [`pdf.header-cell`](https://typst.app/docs/reference/pdf/header-cell/) function to mark a cell as a header cell. Likewise, you can use [`pdf.data-cell`](https://typst.app/docs/reference/pdf/data-cell/) to mark cells in this function as data cells. Note that these functions are not final and thus only available when you enable the `a11y-extras` feature (see the [PDF module documentation](https://typst.app/docs/reference/pdf/) for details).
 * 
 * 
 */
@SerialName("table.header")
data class TTableHeader(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-header](https://typst.app/docs/reference/model/table/#definitions-header)
     * 
     * The cells and lines within the header.
     * 
     * Required, positional, variadic; Typst type: content
     */
    @all:Variadic @all:Positional val children: TArray<TContent>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-header](https://typst.app/docs/reference/model/table/#definitions-header)
     * 
     * Whether this header should be repeated across pages.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val repeat: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/table/#definitions-header](https://typst.app/docs/reference/model/table/#definitions-header)
     * 
     * The level of the header. Must not be zero.
     * 
     * This allows repeating multiple headers at once. Headers with different levels can repeat together, as long as they have ascending levels.
     * 
     * Notably, when a header with a lower level starts repeating, all higher or equal level headers stop repeating (they are "replaced" by the new header).
     * 
     * Settable; Typst type: int
     */
    @all:Settable val level: TInt? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("table.header")
    }
}


@SerialName("set-table.header")
data class TSetTableHeader(
    override val internals: SetRuleInternals? = null,
    val repeat: TBool? = null,
    val level: TInt? = null
) : TSetRule()
