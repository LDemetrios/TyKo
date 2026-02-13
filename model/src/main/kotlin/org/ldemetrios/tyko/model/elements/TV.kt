package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/v/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/v/](https://typst.app/docs/reference/layout/v/)
 * 
 * Inserts vertical spacing into a flow of blocks.
 * 
 * The spacing can be absolute, relative, or fractional. In the last case, the remaining space on the page is distributed among all fractional spacings according to their relative fractions.
 * 
 * **_Example_**
 * 
 * ```typ
 * #grid(
 *   rows: 3cm,
 *   columns: 6,
 *   gutter: 1fr,
 *   [A #parbreak() B],
 *   [A #v(0pt) B],
 *   [A #v(10pt) B],
 *   [A #v(0pt, weak: true) B],
 *   [A #v(40%, weak: true) B],
 *   [A #v(1fr) B],
 * )
 * ```
 * <img src="https://typst.app/assets/docs/DNC2m_0X9s5xLmHMABxCvgAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("v")
data class TV(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/v/](https://typst.app/docs/reference/layout/v/)
     * 
     * How much spacing to insert.
     * 
     * Required, positional; Typst type: relative|fraction
     */
    @all:Positional val amount: Spacing,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/v/](https://typst.app/docs/reference/layout/v/)
     * 
     * If `true`, the spacing collapses at the start or end of a flow. Moreover, from multiple adjacent weak spacings all but the largest one collapse. Weak spacings will always collapse adjacent paragraph spacing, even if the paragraph spacing is larger.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val weak: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("v")
    }
}


@SerialName("set-v")
data class TSetV(
    override val internals: SetRuleInternals? = null,
    val weak: TBool? = null
) : TSetRule()
