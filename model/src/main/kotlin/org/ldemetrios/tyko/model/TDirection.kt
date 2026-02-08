package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("direction")
enum class TDirection : TValue, Smart<TDirection>, Option<TDirection> {
    @SerialName("ltr")
    LTR,
    @SerialName("rtl")
    RTL,
    @SerialName("btt")
    BTT,
    @SerialName("ttb")
    TTB;

    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.DIRECTION
    }

    override fun repr(sb: StringBuilder)  {
        sb.append(name.lowercase())
    }
}
