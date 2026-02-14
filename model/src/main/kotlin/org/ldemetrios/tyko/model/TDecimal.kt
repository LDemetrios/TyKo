package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/foundations/decimal/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/decimal/](https://typst.app/docs/reference/foundations/decimal/)
 * 
 * A fixed-point decimal number type.
 * 
 * This type should be used for precise arithmetic operations on numbers represented in base 10. A typical use case is representing currency.
 * 
 * **_Example_**
 * 
 * ```typ
 * Decimal: #(decimal("0.1") + decimal("0.2")) \
 * Float: #(0.1 + 0.2)
 * ```
 * <img src="https://typst.app/assets/docs/W31Kvh6BvfIgTgIeq2uIEQAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Construction and casts_**
 * 
 * To create a decimal number, use the `decimal(string)` constructor, such as in `decimal("3.141592653")`__(note the double quotes!)__. This constructor preserves all given fractional digits, provided they are representable as per the limits specified below (otherwise, an error is raised).
 * 
 * You can also convert any [integer](https://typst.app/docs/reference/foundations/int/) to a decimal with the `decimal(int)` constructor, e.g. `decimal(59)`. However, note that constructing a decimal from a [floating-point number](https://typst.app/docs/reference/foundations/float/), while supported, __is an imprecise conversion and therefore discouraged.__ A warning will be raised if Typst detects that there was an accidental `float` to `decimal` cast through its constructor, e.g. if writing `decimal(3.14)` (note the lack of double quotes, indicating this is an accidental `float` cast and therefore imprecise). It is recommended to use strings for constant decimal values instead (e.g. `decimal("3.14")`).
 * 
 * The precision of a `float` to `decimal` cast can be slightly improved by rounding the result to 15 digits with [`calc.round`](https://typst.app/docs/reference/foundations/calc/#functions-round), but there are still no precision guarantees for that kind of conversion.
 * 
 * **_Operations_**
 * 
 * Basic arithmetic operations are supported on two decimals and on pairs of decimals and integers.
 * 
 * Built-in operations between `float` and `decimal` are not supported in order to guard against accidental loss of precision. They will raise an error instead.
 * 
 * Certain `calc` functions, such as trigonometric functions and power between two real numbers, are also only supported for `float` (although raising `decimal` to integer exponents is supported). You can opt into potentially imprecise operations with the `float(decimal)` constructor, which casts the `decimal` number into a `float`, allowing for operations without precision guarantees.
 * 
 * **_Displaying decimals_**
 * 
 * To display a decimal, simply insert the value into the document. To only display a certain number of digits, [round](https://typst.app/docs/reference/foundations/calc/#functions-round) the decimal first. Localized formatting of decimals and other numbers is not yet supported, but planned for the future.
 * 
 * You can convert decimals to strings using the [`str`](https://typst.app/docs/reference/foundations/str/) constructor. This way, you can post-process the displayed representation, e.g. to replace the period with a comma (as a stand-in for proper built-in localization to languages that use the comma).
 * 
 * **_Precision and limits_**
 * 
 * A `decimal` number has a limit of 28 to 29 significant base-10 digits. This includes the sum of digits before and after the decimal point. As such, numbers with more fractional digits have a smaller range. The maximum and minimum `decimal` numbers have a value of `79228162514264337593543950335` and `-79228162514264337593543950335` respectively. In contrast with [`float`](https://typst.app/docs/reference/foundations/float/), this type does not support infinity or NaN, so overflowing or underflowing operations will raise an error.
 * 
 * Typical operations between `decimal` numbers, such as addition, multiplication, and [power](https://typst.app/docs/reference/foundations/calc/#functions-pow) to an integer, will be highly precise due to their fixed-point representation. Note, however, that multiplication and division may not preserve all digits in some edge cases: while they are considered precise, digits past the limits specified above are rounded off and lost, so some loss of precision beyond the maximum representable digits is possible. Note that this behavior can be observed not only when dividing, but also when multiplying by numbers between 0 and 1, as both operations can push a number's fractional digits beyond the limits described above, leading to rounding. When those two operations do not surpass the digit limits, they are fully precise.
 */
@SerialName("decimal")
data class TDecimal(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/foundations/decimal/](https://typst.app/docs/reference/foundations/decimal/)
     * 
     * The value that should be converted to a decimal.
     * 
     * Required, positional; Typst type: bool|int|float|str|decimal
     */
    @all:Positional val value: TStr
) : TValue {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.DECIMAL
    }
}
