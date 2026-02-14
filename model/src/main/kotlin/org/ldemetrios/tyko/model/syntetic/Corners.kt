package org.ldemetrios.tyko.model

import kotlin.reflect.KType
import kotlin.reflect.typeOf


/**
 * Defines the synthetic dictionary type with specific keys.
 * See usage at https://typst.app/docs/reference/visualize/rect/#parameters-radius.
 */
sealed interface Corners<out T : IntoValue> : IntoValue {
    val topLeft: T?
    val topRight: T?
    val bottomRight: T?
    val bottomLeft: T?

    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): Corners<T> = when (value) {
            is TDict<*> -> CornersImpl.fromValue(value)
            is T -> value as CornersSplat<T>
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): Corners<*> = when (value) {
            is TDict<*> -> CornersImpl.fromValue(value, type)
            else -> {
                val arg = type.arguments.firstOrNull()?.type
                if (arg == null) value as CornersSplat<*>
                else value.into(arg) as CornersSplat<*>
            }
        }
    }
}

sealed interface CornersSplat<out Self : CornersSplat<Self>> : Corners<Self> {
    override val topLeft: Self get() = this as Self
    override val topRight: Self get() = this as Self
    override val bottomRight: Self get() = this as Self
    override val bottomLeft: Self get() = this as Self
}

@SerialName("dict")
data class CornersImpl<out S : IntoValue>(
    @all:SerialName("top-left") override val topLeft: S?,
    @all:SerialName("top-right") override val topRight: S?,
    @all:SerialName("bottom-right") override val bottomRight: S?,
    @all:SerialName("bottom-left") override val bottomLeft: S?,
) : IntoValue,
    IntoDict<S>, Corners<S>, ArrayOrSingle<CornersImpl<S>>, Option<CornersImpl<S>>, Smart<CornersImpl<S>> {
    override fun intoValue(): TDict<S> = TDict(
        mapOfNotNullValues(
            "top-left" to topLeft,
            "top-right" to topRight,
            "bottom-right" to bottomRight,
            "bottom-left" to bottomLeft,
        )
    )

    companion object {
        fun <T : IntoValue> splat(x: T) = CornersImpl(x, x, x, x)

        inline fun <reified T : IntoValue> fromValue(value: TValue): CornersImpl<T> {
            val dict = value as TDict<*>
            val topLeft = ((dict["top-left"] ?: dict["top"] ?: dict["left"] ?: dict["rest"]) as TValue?)?.into<T>()
            val topRight = ((dict["top-right"] ?: dict["top"] ?: dict["right"] ?: dict["rest"]) as TValue?)?.into<T>()
            val bottomRight =
                ((dict["bottom-right"] ?: dict["bottom"] ?: dict["right"] ?: dict["rest"]) as TValue?)?.into<T>()
            val bottomLeft =
                ((dict["bottom-left"] ?: dict["bottom"] ?: dict["left"] ?: dict["rest"]) as TValue?)?.into<T>()
            return CornersImpl(topLeft, topRight, bottomRight, bottomLeft)
        }

        fun fromValue(value: TValue, type: KType): CornersImpl<*> {
            val arg = type.arguments.firstOrNull()?.type
            val dict = value as TDict<*>
            val topLeft = ((dict["top-left"] ?: dict["top"] ?: dict["left"] ?: dict["rest"]) as TValue?)
                ?.into(arg ?: typeOf<IntoValue>())
            val topRight = ((dict["top-right"] ?: dict["top"] ?: dict["right"] ?: dict["rest"]) as TValue?)
                ?.into(arg ?: typeOf<IntoValue>())
            val bottomRight = ((dict["bottom-right"] ?: dict["bottom"] ?: dict["right"] ?: dict["rest"]) as TValue?)
                ?.into(arg ?: typeOf<IntoValue>())
            val bottomLeft = ((dict["bottom-left"] ?: dict["bottom"] ?: dict["left"] ?: dict["rest"]) as TValue?)
                ?.into(arg ?: typeOf<IntoValue>())
            return CornersImpl(topLeft, topRight, bottomRight, bottomLeft)
        }
    }
}


