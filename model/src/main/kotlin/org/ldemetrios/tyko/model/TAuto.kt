package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("auto")
object TAuto : TValue, Smart<Nothing>, TSmartquoteLevel, ArrayOrSingle<Smart<Nothing>>, TImageFormat,
MarginSplat<TAuto> {
    override fun type(): TType = TYPE

    val TYPE = TType.AUTO

    override fun repr(sb: StringBuilder) {
        sb.append("auto")
    }
}
