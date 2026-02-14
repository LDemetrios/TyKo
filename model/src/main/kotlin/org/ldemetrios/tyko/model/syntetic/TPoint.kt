package org.ldemetrios.tyko.model

import kotlin.reflect.KType


@SerialName("array")
data class TPoint<out T : IntoValue>(val x: T, val y: T) : IntoValue, Self<TPoint<T>>, TCurveControl<T> {
    override fun intoValue(): TArray<T> = TArray(listOf(x, y))

    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): TPoint<T> {
            val array = value as TArray<*>
            require(array.size == 2) { "Point requires 2 items, got ${array.size}" }
            val x = (array[0] as TValue).into<T>()
            val y = (array[1] as TValue).into<T>()
            return TPoint(x, y)
        }

        fun fromValue(value: TValue, type: KType): TPoint<*> {
            val arg = type.arguments.firstOrNull()?.type
            val array = value as TArray<*>
            require(array.size == 2) { "Point requires 2 items, got ${array.size}" }
            return if (arg == null) {
                TPoint(array[0] as IntoValue, array[1] as IntoValue)
            } else {
                val x = (array[0] as TValue).into(arg)
                val y = (array[1] as TValue).into(arg)
                TPoint(x, y)
            }
        }
    }
}
