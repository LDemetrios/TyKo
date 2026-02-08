package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/grid/#definitions-footer
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A repeatable grid footer.
 * 
 * Just like the [`grid.header`](https://typst.app/docs/reference/layout/grid/#definitions-header) element, the footer can repeat itself on every page of the grid.
 * 
 * No other grid cells may be placed after the footer.
 */
@SerialName("grid.footer")
data class TGridFooter(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The cells and lines within the footer.
     * 
     * Required, positional, variadic; Typst type: content
     */
    @all:Variadic @all:Positional val children: TArray<TContent>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether this footer should be repeated across pages.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val repeat: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("grid.footer")
    }
}


@SerialName("set-grid.footer")
data class TSetGridFooter(
    override val internals: SetRuleInternals? = null,
    val repeat: TBool? = null
) : TSetRule()
