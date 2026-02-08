package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/grid/#definitions-header
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A repeatable grid header.
 * 
 * If `repeat` is set to `true`, the header will be repeated across pages. For an example, refer to the [`table.header`](https://typst.app/docs/reference/model/table/#definitions-header) element and the [`grid.stroke`](https://typst.app/docs/reference/layout/grid/#parameters-stroke) parameter.
 */
@SerialName("grid.header")
data class TGridHeader(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The cells and lines within the header.
     * 
     * Required, positional, variadic; Typst type: content
     */
    @all:Variadic @all:Positional val children: TArray<TContent>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Whether this header should be repeated across pages.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val repeat: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
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
        val ELEM = TElement("grid.header")
    }
}


@SerialName("set-grid.header")
data class TSetGridHeader(
    override val internals: SetRuleInternals? = null,
    val repeat: TBool? = null,
    val level: TInt? = null
) : TSetRule()
