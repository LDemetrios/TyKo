package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.TAlternation
import org.ldemetrios.tyko.model.TColorComponent
import org.ldemetrios.tyko.model.TrackSize

//!https://typst.app/docs/reference/foundations/int/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/int/](https://typst.app/docs/reference/foundations/int/)
 * 
 * A whole number.
 * 
 * The number can be negative, zero, or positive. As Typst uses 64 bits to store integers, integers cannot be smaller than `-9223372036854775808` or larger than `9223372036854775807`. Integer literals are always positive, so a negative integer such as `-1` is semantically the negation `-` of the positive literal `1`. A positive integer greater than the maximum value and a negative integer less than or equal to the minimum value cannot be represented as an integer literal, and are instead parsed as a `float`. The minimum integer value can still be obtained through integer arithmetic.
 * 
 * The number can also be specified as hexadecimal, octal, or binary by starting it with a zero followed by either `x`, `o`, or `b`.
 * 
 * You can convert a value to an integer with this type's constructor.
 * 
 * **_Example_**
 * 
 * ```typ
 * #(1 + 2) \
 * #(2 - 5) \
 * #(3 + 4 < 8)
 * 
 * #0xff \
 * #0o10 \
 * #0b1001
 * ```
 * <img src="https://typst.app/assets/docs/wfpxRJDZrNeGDA3RjEgFJgAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("int")
data class TInt(val value: Long) : TValue, TColorComponent, TTextWeight, TMatAugment, TrackSize, Smart<TInt>,
    TAlternation,
    ArrayOrSingle<TInt>, Option<TInt>, Computable<TInt> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.INT
    }

    override fun repr(sb: StringBuilder) {
        sb.append(value)
    }
}

/**
 * Convenience extension converting `Long` into the corresponding Typst value.
 */
val Long.t get() = TInt(this)
/**
 * Convenience extension converting `Int` into the corresponding Typst value.
 */
val Int.t get() = TInt(this.toLong())
/**
 * Convenience extension converting `Short` into the corresponding Typst value.
 */
val Short.t get() = TInt(this.toLong())
/**
 * Convenience extension converting `Byte` into the corresponding Typst value.
 */
val Byte.t get() = TInt(this.toLong())
