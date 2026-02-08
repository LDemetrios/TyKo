package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("module")
data class TModule(
    val name: TStr?,
    val scope: TDict<TValue>,
    val content: TContent,
) : TValue {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.MODULE
    }

    override fun repr(sb: StringBuilder) {
        sb.append("std.")
        sb.append(name!!.value)
    }
}
