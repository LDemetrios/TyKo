package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@SerialName("int")
data class TInt(val value: Long) : TValue, TColorComponent, TTextWeight, TMatAugment, TrackSize, Smart<TInt>,
    TAlternation,
    ArrayOrSingle<TInt>, Option<TInt>, Computable<TInt> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.INT
    }

    override fun repr(sb: StringBuilder) {
        sb.append(value)
    }
}

val Long.t get() = TInt(this)
val Int.t get() = TInt(this.toLong())
val Short.t get() = TInt(this.toLong())
val Byte.t get() = TInt(this.toLong())
