package org.ldemetrios.tyko.model

import kotlin.reflect.KType


/**
 * A control point for [TCurve] components, or lack there of.
 */
sealed interface TCurveControl<out T : IntoValue> : IntoValue {
    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): TCurveControl<T> = when (value) {
            is TArray<*> -> TPoint.fromValue<T>(value)
            TNone -> TNone
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): TCurveControl<*> = when (value) {
            is TArray<*> -> {
                val arg = type.arguments.firstOrNull()?.type
                if (arg == null) TPoint.fromValue<IntoValue>(value) else TPoint.fromValue(value, arg)
            }
            TNone -> TNone
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}
