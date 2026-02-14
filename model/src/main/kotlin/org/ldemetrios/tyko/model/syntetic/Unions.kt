package org.ldemetrios.tyko.model

import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class UnionType(val value: Array<String>)

@UnionType(["style", "content", "symbol"])
sealed class SequenceElement : TValue

@UnionType(["path", "bytes"])
sealed interface DataSource : TValue, DataSourceOrPreset<Nothing>

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

@UnionType(["content", "func", "style"])
sealed interface TTransform : TValue

@UnionType(["int", "relative", "fraction"])
sealed interface TrackSize : TValue

@UnionType(["relative", "fraction"])
sealed interface Spacing : TrackSize, TStackComponent

@UnionType(["str", "function"])
sealed interface Numbering : TValue

@UnionType(["label", "content"])
sealed interface Attribution : TValue

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
            is TArray<*> -> TArray.Companion.fromValue<T>(value)
            is TDict<*> -> TDict.Companion.fromValue<T>(value)
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): TCollection<*> {
            val arg = type.arguments.firstOrNull()?.type ?: typeOf<IntoValue>()
            return when (value) {
                is TArray<*> -> TArray.Companion.fromValue(value, arg)
                is TDict<*> -> TDict.Companion.fromValue(value, arg)
                else -> throw AssertionError("Can't convert from $value")
            }
        }
    }
}
