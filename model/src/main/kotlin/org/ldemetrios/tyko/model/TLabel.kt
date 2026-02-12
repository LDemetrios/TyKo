package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("label")
data class TLabel(@all: Positional val value: TStr) : TValue, TSelectable<TValue>, Option<TLabel>, Attribution,TCounterKey, TLinkDestination {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.LABEL
    }
}
