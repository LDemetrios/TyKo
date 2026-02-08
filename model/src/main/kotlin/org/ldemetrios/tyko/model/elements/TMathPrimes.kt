package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/primes/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Grouped primes.
 * 
 * ```typ
 * $ a'''_b = a^'''_b $
 * ```
 * <img src="https://typst.app/assets/docs/uHgNvego3SyqChIc3iZ9sQAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This function has dedicated syntax: use apostrophes instead of primes. They will automatically attach to the previous element, moving superscripts to the next level.
 */
@SerialName("math.primes")
data class TMathPrimes(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The number of grouped primes.
     * 
     * Required, positional; Typst type: int
     */
    @all:Positional val count: TInt,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.primes")
    }
}
