package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/math/op/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/op/](https://typst.app/docs/reference/math/op/)
 * 
 * A text operator in an equation.
 * 
 * **_Example_**
 * 
 * ```typ
 * $ tan x = (sin x)/(cos x) $
 * $ op("custom",
 *      limits: #true)_(n->oo) n $
 * ```
 * <img src="https://typst.app/assets/docs/n9yefElmfwTi92ejfLzhZwAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Predefined Operators_**
 * 
 * Typst predefines the operators `arccos`, `arcsin`, `arctan`, `arg`, `cos`, `cosh`, `cot`, `coth`, `csc`, `csch`, `ctg`, `deg`, `det`, `dim`, `exp`, `gcd`, `lcm`, `hom`, `id`, `im`, `inf`, `ker`, `lg`, `lim`, `liminf`, `limsup`, `ln`, `log`, `max`, `min`, `mod`, `Pr`, `sec`, `sech`, `sin`, `sinc`, `sinh`, `sup`, `tan`, `tanh`, `tg` and `tr`.
 */
@SerialName("math.op")
data class TMathOp(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/op/](https://typst.app/docs/reference/math/op/)
     * 
     * The operator's text.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val text: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/op/](https://typst.app/docs/reference/math/op/)
     * 
     * Whether the operator should show attachments as limits in display mode.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val limits: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.op")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TMathOp]
 */
@SerialName("set-math.op")
data class TSetMathOp(
    override val internals: SetRuleInternals? = null,
    val limits: TBool? = null
) : TSetRule()
