package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


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

val Double.t get() = TFloat(this)
val Float.t get() = TFloat(this.toDouble())
