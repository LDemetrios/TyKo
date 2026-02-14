package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/layout/angle/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/angle/](https://typst.app/docs/reference/layout/angle/)
 * 
 * An angle describing a rotation.
 * 
 * Typst supports the following angular units:
 * 
 * - Degrees: `180deg`
 * - Radians: `3.14rad`
 * 
 * **_Example_**
 * 
 * ```typ
 * #rotate(10deg)[Hello there!]
 * ```
 * <img src="https://typst.app/assets/docs/bDyrcLTzr2eRmGWeZRN2_QAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("angle")
data class TAngle(/** Value of the angle, in `rad`ians */ val value: TFloat) : TValue, Computable<TAngle>, Option<TAngle>, Smart<TAngle> {
    override fun type(): TType = TYPE

    val rad get() = value
    val deg get() = TFloat(value.value * 180 / Math.PI)

    companion object {
        val TYPE = TType.ANGLE
    }

    override fun repr(sb: StringBuilder)  {
        value.repr(sb)
        sb.append("rad")
    }
}

/**
 * Interprets a float as angle measured in radians.
 */
val TFloat.rad get() = TAngle(this)
/**
 * Interprets a float as angle measured in degrees.
 */
val TFloat.deg get() = TAngle(TFloat(this.value * Math.PI / 180))
