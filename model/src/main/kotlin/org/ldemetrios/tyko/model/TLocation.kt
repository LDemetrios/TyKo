package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("location")
data class TLocation(val id: TStr) : TValue, TSelectable<TValue>, TLinkDestination {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.LOCATION
    }

    override fun repr(sb: StringBuilder) {
        TODO()
    }
}
