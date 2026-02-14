package org.ldemetrios.tyko.model

import kotlin.collections.mapNotNull
import kotlin.collections.orEmpty
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField


//!https://typst.app/docs/reference/math/frac/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/frac/](https://typst.app/docs/reference/math/frac/)
 * 
 * A mathematical fraction.
 * 
 * **_Example_**
 * 
 * ```typ
 * $ 1/2 < (x+1)/2 $
 * $ ((x+1)) / 2 = frac(a, b) $
 * ```
 * <img src="https://typst.app/assets/docs/9RFsr-VSObielPb4Nrr-zQAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This function also has dedicated syntax: Use a slash to turn neighbouring expressions into a fraction. Multiple atoms can be grouped into a single expression using round grouping parentheses. Such parentheses are removed from the output, but you can nest multiple to force them.
 */
@SerialName("math.frac")
data class TMathFrac(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/frac/](https://typst.app/docs/reference/math/frac/)
     * 
     * The fraction's numerator.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val num: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/frac/](https://typst.app/docs/reference/math/frac/)
     * 
     * The fraction's denominator.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val denom: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/frac/](https://typst.app/docs/reference/math/frac/)
     * 
     * How the fraction should be laid out.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"vertical"` | Stacked numerator and denominator with a bar. |
     * | `"skewed"` | Numerator and denominator separated by a slash. |
     * | `"horizontal"` | Numerator and denominator placed inline and parentheses are not absorbed. |
     * 
     * Settable; Typst type: str
     */
    @all:Settable val style: TStr? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.frac")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TMathFrac]
 */
@SerialName("set-math.frac")
data class TSetMathFrac(
    override val internals: SetRuleInternals? = null,
    val style: TStr? = null
) : TSetRule()
