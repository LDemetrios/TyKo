package org.ldemetrios.tyko.model

import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class UnionType(val value: Array<String>)

sealed interface DataSourceOrPreset<out E : DataSourceOrPreset<E>> : IntoValue {
    companion object {
        inline fun <reified E : DataSourceOrPreset<E>> fromValue(value: TValue): DataSourceOrPreset<E> = when (value) {
            is TBytes -> value
            else -> try {
                value.into<E>()
            } catch (e: AssertionError) {
                if (value is TStr) TPath(value.value)
                else throw AssertionError("Can't convert from $value", e)
            }
        }

        fun fromValue(value: TValue, type: KType): DataSourceOrPreset<*> {
            val arg = type.arguments.firstOrNull()?.type
            return if (arg == null) {
                if (value is TStr) TPath(value.value) else value as DataSourceOrPreset<*>
            } else {
                try {
                    value.into(arg) as DataSourceOrPreset<*>
                } catch (e: AssertionError) {
                    if (value is TStr) TPath(value.value) else throw AssertionError("Can't convert from $value", e)
                }
            }
        }
    }
}

@UnionType(["none", "datetime"])
sealed interface DocumentDatetime : TValue

@UnionType(["path", "bytes"])
sealed interface DataSource : TValue, DataSourceOrPreset<Nothing> {
    companion object {
         fun fromValue(value: TValue): DataSource = when (value) {
            is TBytes -> value
            is TStr -> TPath(value.value)
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@UnionType(["int", "ratio"])
sealed interface TColorComponent : TValue

@UnionType(["color", "gradient", "tiling"])
sealed interface TPaint : TValue, TStroke {
    override val paint: Smart<TPaint> get() = this as Smart<TPaint>
    override val thickness: Smart<TLength> get() = TAuto
    override val cap: Smart<LineCap> get() = TAuto
    override val join: Smart<LineJoin> get() = TAuto
    override val dash: Smart<Dash> get() = TAuto
    override val miterLimit: Smart<TFloat> get() = TAuto
}

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

@UnionType(["content", "func", "style"])
sealed interface TTransform : TValue

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

@UnionType(["int", "relative", "fraction"])
sealed interface TrackSize : TValue

@UnionType(["relative", "fraction"])
sealed interface Spacing : TrackSize, TStackComponent

@UnionType(["str", "function"])
sealed interface Numbering : TValue

@UnionType(["label", "content"])
sealed interface Attribution : TValue

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

sealed interface TTextWeight : IntoValue {
    companion object {
        inline fun fromValue(value: TValue): TTextWeight = when (value) {
            is TInt -> value
            is TStr -> TTextWeightPreset.fromValue(value)
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@UnionType(["content", "relative", "fraction"])
sealed interface TStackComponent : TValue

@UnionType(["content", "str"])
sealed interface TAttachment : TValue


@UnionType(["str", "symbol"])
sealed interface TSymbolLike : TValue

@UnionType(["bool", "int"])
sealed interface TAlternation : TValue

@UnionType(["array", "dict"])
sealed interface TCollection<out E : IntoValue> : TValue, IntoCollection<E> {
    override fun intoValue(): TCollection<E> = this

    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): TCollection<T> = when (value) {
            is TArray<*> -> TArray.fromValue<T>(value)
            is TDict<*> -> TDict.fromValue<T>(value)
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): TCollection<*> {
            val arg = type.arguments.firstOrNull()?.type ?: typeOf<IntoValue>()
            return when (value) {
                is TArray<*> -> TArray.fromValue(value, arg)
                is TDict<*> -> TDict.fromValue(value, arg)
                else -> throw AssertionError("Can't convert from $value")
            }
        }
    }
}
