package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.TSymbolLike


//!https://typst.app/docs/reference/math/cases/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/cases/](https://typst.app/docs/reference/math/cases/)
 * 
 * A case distinction.
 * 
 * Content across different branches can be aligned with the `&` symbol.
 * 
 * **_Example_**
 * 
 * ```typ
 * $ f(x, y) := cases(
 *   1 "if" (x dot y)/2 <= 0,
 *   2 "if" x "is even",
 *   3 "if" x in NN,
 *   4 "else",
 * ) $
 * ```
 * <img src="https://typst.app/assets/docs/0X1AFPDieBd9jiawKpc0-AAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("math.cases")
data class TMathCases(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/cases/](https://typst.app/docs/reference/math/cases/)
     * 
     * The branches of the case distinction.
     * 
     * Required, positional, variadic; Typst type: content
     */
    @all:Variadic @all:Positional  val children: TArray<TContent>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/cases/](https://typst.app/docs/reference/math/cases/)
     * 
     * The delimiter to use.
     * 
     * Can be a single character specifying the left delimiter, in which case the right delimiter is inferred. Otherwise, can be an array containing a left and a right delimiter.
     * 
     * Settable; Typst type: none|str|array|symbol
     */
    @all:Settable val delim: ArrayOrSingle<Option<TSymbolLike>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/cases/](https://typst.app/docs/reference/math/cases/)
     * 
     * Whether the direction of cases should be reversed.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val reverse: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/cases/](https://typst.app/docs/reference/math/cases/)
     * 
     * The gap between branches.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val gap: TRelative? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.cases")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TMathCases]
 */
@SerialName("set-math.cases")
data class TSetMathCases(
    override val internals: SetRuleInternals? = null,
    val delim: ArrayOrSingle<Option<TSymbolLike>>? = null,
    val reverse: TBool? = null,
    val gap: TRelative? = null
) : TSetRule()
