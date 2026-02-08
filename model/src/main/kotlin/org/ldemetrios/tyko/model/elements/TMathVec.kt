package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/vec/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A column vector.
 * 
 * Content in the vector's elements can be aligned with the [`align`](https://typst.app/docs/reference/math/vec/#parameters-align) parameter, or the `&` symbol.
 * 
 * This function is for typesetting vector components. To typeset a symbol that represents a vector, [`arrow`](https://typst.app/docs/reference/math/accent/) and [`bold`](https://typst.app/docs/reference/math/styles/#functions-bold) are commonly used.
 * 
 * **_Example_**
 * 
 * ```typ
 * $ vec(a, b, c) dot vec(1, 2, 3)
 *     = a + 2b + 3c $
 * ```
 * <img src="https://typst.app/assets/docs/LnRm06lLMggD8fCQZdA66QAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("math.vec")
data class TMathVec(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The elements of the vector.
     * 
     * Required, positional, variadic; Typst type: content
     */
    @all:Variadic @all:Positional val children: TArray<TContent>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The delimiter to use.
     * 
     * Can be a single character specifying the left delimiter, in which case the right delimiter is inferred. Otherwise, can be an array containing a left and a right delimiter.
     * 
     * Settable; Typst type: none|str|array|symbol
     */
    @all:Settable val delim: ArrayOrSingle<Option<TSymbolLike>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The horizontal alignment that each element should have.
     * 
     * Settable; Typst type: alignment
     */
    @all:Settable val align: TAlignment? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The gap between elements.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val gap: TRelative? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.vec")
    }
}


@SerialName("set-math.vec")
data class TSetMathVec(
    override val internals: SetRuleInternals? = null,
    val delim: ArrayOrSingle<Option<TSymbolLike>>? = null,
    val align: TAlignment? = null,
    val gap: TRelative? = null
) : TSetRule()
