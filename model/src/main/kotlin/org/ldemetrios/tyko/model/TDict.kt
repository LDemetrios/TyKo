package org.ldemetrios.tyko.model

import kotlin.reflect.KType

import kotlinx.serialization.Serializable


@SerialName("dict")
data class TDict<out V: IntoValue>(val value: Map<String, V>) : TValue, Map<String, V> by value, IntoDict<V>, TCollection<V> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.DICT

        inline fun <reified V: IntoValue> fromValue(value: TValue): TDict<V> = when (value) {
            is TDict<*> -> TDict(value.value.mapValues { it.value.intoValue().into<V>() })
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): TDict<*> {
            val arg = type.arguments.firstOrNull()?.type ?: return fromValue<IntoValue>(value)
            val dict = value as TDict<*>
            return TDict(dict.value.mapValues { (it.value as TValue).into(arg) })
        }
    }

    override fun intoValue(): TDict<V> = this

    override fun repr(sb: StringBuilder) {
        if (value.isEmpty()) {
            sb.append("(:)")
        } else {
            sb.append("(")
            value.forEach { (key, value) ->
                sb.append(key)
                sb.append(":")
                value.repr(sb)
                sb.append(", ")
            }
            sb.append(")")
        }
    }
}
