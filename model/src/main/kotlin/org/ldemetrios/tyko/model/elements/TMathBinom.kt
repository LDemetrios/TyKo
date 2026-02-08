package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/binom/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A binomial expression.
 * 
 * **_Example_**
 * 
 * ```typ
 * $ binom(n, k) $
 * $ binom(n, k_1, k_2, k_3, ..., k_m) $
 * ```
 * <img src="https://typst.app/assets/docs/x7e1yoGny67cX0IzBxp69AAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("math.binom")
data class TMathBinom(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The binomial's upper index.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val upper: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The binomial's lower index.
     * 
     * Required, positional, variadic; Typst type: content
     */
    @all:Variadic @all:Positional val lower: TArray<TContent>,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.binom")
    }
}
