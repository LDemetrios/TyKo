package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.TBytes
import org.ldemetrios.tyko.model.TPath
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.into
import kotlin.reflect.KType

/**
 * Either some built-in value (&lt;E>), or a [DataSource]
 */
sealed interface DataSourceOrPreset<out E : DataSourceOrPreset<E>> : IntoValue {
    companion object {
        inline fun <reified E : DataSourceOrPreset<E>> fromValue(value: TValue): DataSourceOrPreset<E> = when (value) {
            is TPath -> value
            is TBytes -> value
            else -> value.into<E>()
        }

        fun fromValue(value: TValue, type: KType): DataSourceOrPreset<*> {
            val arg = type.arguments.firstOrNull()?.type
            return if (arg == null) {
                value as DataSourceOrPreset<*>
            } else {
                value.into(arg) as DataSourceOrPreset<*>
            }
        }
    }
}

/**
 * Either single `T`, or an array of ones
 */
sealed interface ArrayOrSingle<out T : IntoValue> : IntoValue, Progression<T> {
    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): ArrayOrSingle<T> = when (value) {
            is TArray<*> -> TArray.fromValue<T>(value) as ArrayOrSingle<T>
            else -> value.into<T>() as ArrayOrSingle<T>
        }

        fun fromValue(value: TValue, type: KType): ArrayOrSingle<*> {
            val arg = type.arguments.firstOrNull()?.type
            return when (value) {
                is TArray<*> -> if (arg == null) value else TArray.fromValue(value, arg)
                else -> if (arg == null) value as ArrayOrSingle<*> else value.into(arg) as ArrayOrSingle<*>
            }
        }
    }
}

/**
 * Either single `T`, array of ones, or a function that generates on of them.
 */
sealed interface Progression<out T : IntoValue> : IntoValue {
    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): Progression<T> = when (value) {
            is TFunc -> value
            else -> ArrayOrSingle.fromValue<T>(value)
        }

        fun fromValue(value: TValue, type: KType): Progression<*> = when (value) {
            is TFunc -> value
            else -> ArrayOrSingle.fromValue(value, type)
        }
    }
}

/**
 * Either T, or [TNone]
 */
sealed interface Option<out T : IntoValue> : IntoValue {
    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): Option<T> = when (value) {
            is TNone -> TNone
            else -> value.into<T>() as Option<T>
        }

        fun fromValue(value: TValue, type: KType): Option<*> = when (value) {
            is TNone -> TNone
            else -> {
                val arg = type.arguments.firstOrNull()?.type
                if (arg == null) value as Option<*> else value.into(arg) as Option<*>
            }
        }
    }
}

/**
 * Either T, or a function that computes one.
 */
sealed interface Computable<out T : IntoValue> : IntoValue {
    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): Computable<T> = when (value) {
            is TFunc -> value
            else -> value.into<T>() as Computable<T>
        }

        fun fromValue(value: TValue, type: KType): Computable<*> = when (value) {
            is TFunc -> value
            else -> {
                val arg = type.arguments.firstOrNull()?.type
                if (arg == null) value as Computable<*> else value.into(arg) as Computable<*>
            }
        }
    }
}

/**
 * Either T, or [TAuto]
 */
sealed interface Smart<out T : IntoValue> : IntoValue {
    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): Smart<T> = when (value) {
            is TAuto -> TAuto
            else -> value.into<T>() as Smart<T>
        }

        fun fromValue(value: TValue, type: KType): Smart<*> {
            val arg = type.arguments.firstOrNull()?.type
            return when (value) {
                is TAuto -> TAuto
                else -> if (arg == null) value as Smart<*> else value.into(arg) as Smart<*>
            }
        }
    }
}

sealed interface Self<out T : Self<T>> : Smart<T>, Option<T>, ArrayOrSingle<T>, Computable<T>, MarginSplat<T>,
    SidesSplat<T>, CornersSplat<T>
