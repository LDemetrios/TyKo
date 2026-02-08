package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


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
