package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("none")
data object TNone : TValue, Dash, Smart<TNone>, TCurveControl<Nothing>, DocumentDatetime, ArrayOrSingle<TNone>,
    Option<Nothing>, SidesSplat<TNone> {
    override fun type(): TType = TYPE

    val TYPE = TType.NONE

    override fun repr(sb: StringBuilder) {
        sb.append("none")
    }
}
