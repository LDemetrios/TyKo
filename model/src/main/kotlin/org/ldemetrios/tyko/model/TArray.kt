package org.ldemetrios.tyko.model

import java.util.function.IntFunction
import kotlin.reflect.KType

import kotlinx.serialization.Serializable


@SerialName("array")
data class TArray<out T : IntoValue>(val value: List<T>) : TValue, Option<TArray<T>>, ArrayOrSingle<T>, TCollection<T>,
    Computable<TArray<T>>, List<T> by value {
    override fun type(): TType = TYPE

    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): TArray<T> = when (value) {
            is TArray<*> -> TArray(value.value.map { it.intoValue().into<T>() })
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): TArray<*> {
            val arg = type.arguments.firstOrNull()?.type ?: return fromValue<IntoValue>(value)
            val array = value as TArray<*>
            return TArray(array.value.map { (it as TValue).into(arg) })
        }

        val TYPE = TType.ARRAY
    }

    @Deprecated("")
    override fun <T : Any?> toArray(generator: IntFunction<Array<out T?>?>): Array<out T?>? {
        return super.toArray(generator)
    }

    override fun repr(sb: StringBuilder) {
        sb.append("(")
        value.forEach {
            it.repr(sb)
            sb.append(",")
        }
        sb.append(")")
    }
}

val <T : IntoValue> List<T>.t get() = TArray(this)
