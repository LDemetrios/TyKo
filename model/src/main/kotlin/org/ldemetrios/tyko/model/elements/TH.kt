package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/h/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Inserts horizontal spacing into a paragraph.
 * 
 * The spacing can be absolute, relative, or fractional. In the last case, the remaining space on the line is distributed among all fractional spacings according to their relative fractions.
 * 
 * **_Example_**
 * 
 * ```typ
 * First #h(1cm) Second \
 * First #h(30%) Second
 * ```
 * <img src="https://typst.app/assets/docs/8wL-xYLR6Y7MLlpoIuX_vAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Fractional spacing_**
 * 
 * With fractional spacing, you can align things within a line without forcing a paragraph break (like [`align`](https://typst.app/docs/reference/layout/align/) would). Each fractionally sized element gets space based on the ratio of its fraction to the sum of all fractions.
 * 
 * ```typ
 * First #h(1fr) Second \
 * First #h(1fr) Second #h(1fr) Third \
 * First #h(2fr) Second #h(1fr) Third
 * ```
 * <img src="https://typst.app/assets/docs/pBCqhY9Aheurjnzy2VgPBgAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Mathematical Spacing_**
 * 
 * In [mathematical formulas](https://typst.app/docs/reference/math/), you can additionally use these constants to add spacing between elements: `thin` (1/6 em), `med` (2/9 em), `thick` (5/18 em), `quad` (1 em), `wide` (2 em).
 */
@SerialName("h")
data class TH(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * How much spacing to insert.
     * 
     * Required, positional; Typst type: relative|fraction
     */
    @all:Positional val amount: Spacing,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * If `true`, the spacing collapses at the start or end of a paragraph. Moreover, from multiple adjacent weak spacings all but the largest one collapse.
     * 
     * Weak spacing in markup also causes all adjacent markup spaces to be removed, regardless of the amount of spacing inserted. To force a space next to weak spacing, you can explicitly write `#" "` (for a normal space) or `~` (for a non-breaking space). The latter can be useful to create a construct that always attaches to the preceding word with one non-breaking space, independently of whether a markup space existed in front or not.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val weak: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("h")
    }
}


@SerialName("set-h")
data class TSetH(
    override val internals: SetRuleInternals? = null,
    val weak: TBool? = null
) : TSetRule()
