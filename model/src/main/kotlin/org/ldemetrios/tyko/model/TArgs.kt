package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("args")
data class TArgs<out T : TValue>(val positional: TArray<T>, val named: TDict<T>) : TValue {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.ARGS
    }

    override fun repr(sb: StringBuilder) {
        sb.append("arguments(")
        sb.append("..")
        positional.repr(sb)
        sb.append(", ..")
        named.repr(sb)
        sb.append(")")
    }
}
