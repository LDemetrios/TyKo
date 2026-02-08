package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@SerialName("str")
data class TStr(val value: String) : TValue, TSmartquoteLevel, TFigureKind, TFontDescriptor, Smart<TStr>, TAttachment, TCounterKey,
    TSymbolLike, Option<TStr>, ArrayOrSingle<TStr>, Numbering, TLinkDestination {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.STR
    }

    override fun repr(sb: StringBuilder) {
        sb.append(Json.encodeToString(value))
    }
}

val String.t get() = TStr(this)
