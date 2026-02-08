package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("decimal")
data class TDecimal(@all:Positional val value: TStr) : TValue {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.DECIMAL
    }
}
