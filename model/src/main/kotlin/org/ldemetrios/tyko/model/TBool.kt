package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("bool")
data class TBool(val value: Boolean) : TValue, Smart<TBool>, Option<TBool>, TAlternation {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.BOOL
    }

    override fun repr(sb: StringBuilder)  {
        sb.append(value)
    }
}

val Boolean.t get() = TBool(this)
