package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/foundations/float/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/float/](https://typst.app/docs/reference/foundations/float/)
 * 
 * A floating-point number.
 * 
 * A limited-precision representation of a real number. Typst uses 64 bits to store floats. Wherever a float is expected, you can also pass an [integer](https://typst.app/docs/reference/foundations/int/).
 * 
 * You can convert a value to a float with this type's constructor.
 * 
 * NaN and positive infinity are available as `float.nan` and `float.inf` respectively.
 * 
 * **_Example_**
 * 
 * ```typ
 * #3.14 \
 * #1e4 \
 * #(10 / 4)
 * ```
 * <img src="https://typst.app/assets/docs/Oh7PyPKhSHHcwVH4CSb0KwAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("float")
data class TFloat(val value: Double) : TValue, ArrayOrSingle<TFloat>, Smart<TFloat> {
    override fun type(): TType = TYPE

    companion object {
        internal val ZERO = 0.0.t
        val TYPE = TType.FLOAT
    }

    override fun repr(sb: StringBuilder)  {
        sb.append(value)
    }
}

/**
 * Convenience extension converting `Double` into the corresponding Typst value.
 */
val Double.t get() = TFloat(this)
/**
 * Convenience extension converting `Float` into the corresponding Typst value.
 */
val Float.t get() = TFloat(this.toDouble())
