package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("label")
class TLabel(@all: Positional val value: TStr) : TValue, TSelectable<TValue>, Attribution,TCounterKey, TLinkDestination {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.LABEL
    }
}
