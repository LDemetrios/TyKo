package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/layout/colbreak/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Forces a column break.
 * 
 * The function will behave like a [page break](https://typst.app/docs/reference/layout/pagebreak/) when used in a single column layout or the last column on a page. Otherwise, content after the column break will be placed in the next column.
 * 
 * **_Example_**
 * 
 * ```typ
 * #set page(columns: 2)
 * Preliminary findings from our
 * ongoing research project have
 * revealed a hitherto unknown
 * phenomenon of extraordinary
 * significance.
 * 
 * #colbreak()
 * Through rigorous experimentation
 * and analysis, we have discovered
 * a hitherto uncharacterized process
 * that defies our current
 * understanding of the fundamental
 * laws of nature.
 * ```
 * <img src="https://typst.app/assets/docs/MXyldqpQM7MpLi9gC6sPGAAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("colbreak")
data class TColbreak(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * If `true`, the column break is skipped if the current column is already empty.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val weak: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("colbreak")
    }
}


@SerialName("set-colbreak")
data class TSetColbreak(
    override val internals: SetRuleInternals? = null,
    val weak: TBool? = null
) : TSetRule()
