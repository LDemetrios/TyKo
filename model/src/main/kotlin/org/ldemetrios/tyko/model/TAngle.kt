package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("angle")
data class TAngle(val value: TFloat) : TValue, Computable<TAngle>, Option<TAngle>, Smart<TAngle> {
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

val TFloat.rad get() = TAngle(this)
val TFloat.deg get() = TAngle(TFloat(this.value * Math.PI / 180))
