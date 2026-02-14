package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.Spacing

//!https://typst.app/docs/reference/layout/fraction/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/fraction/](https://typst.app/docs/reference/layout/fraction/)
 * 
 * Defines how the remaining space in a layout is distributed.
 * 
 * Each fractionally sized element gets space based on the ratio of its fraction to the sum of all fractions.
 * 
 * For more details, also see the [h](https://typst.app/docs/reference/layout/h/) and [v](https://typst.app/docs/reference/layout/v/) functions and the [grid function](https://typst.app/docs/reference/layout/grid/).
 * 
 * **_Example_**
 * 
 * ```typ
 * Left #h(1fr) Left-ish #h(2fr) Right
 * ```
 * <img src="https://typst.app/assets/docs/Mh5sjFkAJFlbM1vm_65COgAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("fraction")
data class TFraction(val value: TFloat) : TValue, Spacing, Smart<TFraction>, Option<TFraction>, ArrayOrSingle<TFraction> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.FRACTION
    }

    override fun repr(sb: StringBuilder)  {
        value.repr(sb)
        sb.append("fr")
    }
}
