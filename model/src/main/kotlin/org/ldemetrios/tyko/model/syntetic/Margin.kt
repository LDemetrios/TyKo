package org.ldemetrios.tyko.model

import kotlin.reflect.KType
import kotlin.reflect.typeOf


/**
 * Defines the synthetic dictionary type with specific keys.
 * See usage at https://typst.app/docs/reference/layout/page/#parameters-margin.
 */
sealed interface Margin<out M : IntoValue> : IntoValue {
    val sides: Sides<M>
    val twoSided: TBool

    companion object {
        inline fun <reified M : IntoValue> fromValue(value: TValue) = when (value) {
            is TDict<*> -> MarginImpl.fromValue(value)
            is M -> value as MarginSplat<M>
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): Margin<*> = when (value) {
            is TDict<*> -> MarginImpl.fromValue(value, type)
            else -> {
                val arg = type.arguments.firstOrNull()?.type
                if (arg == null) value as MarginSplat<*>
                else value.into(arg) as MarginSplat<*>
            }
        }
    }
}

sealed interface MarginSplat<out Self : MarginSplat<Self>> : SidesSplat<Self>, Margin<Self> {
    override val sides: Sides<Self> get() = this
    override val twoSided: TBool get() = false.t
}

@SerialName("dict")
data class MarginImpl<out M : IntoValue>(override val sides: Sides<M>, override val twoSided: TBool) : IntoDict<M>,
    Margin<M>, Option<MarginImpl<M>>, Smart<MarginImpl<M>>, ArrayOrSingle<MarginImpl<M>> {
    override fun intoValue(): TDict<M> = if (twoSided.value) {
        TDict(
            mapOfNotNullValues(
                "top" to sides.top,
                "bottom" to sides.bottom,
                "inside" to sides.left,
                "outside" to sides.right,
            )
        )
    } else {
        TDict(
            mapOfNotNullValues(
                "top" to sides.top,
                "bottom" to sides.bottom,
                "left" to sides.left,
                "right" to sides.right,
            )
        )
    }

    companion object {
        inline fun <reified M : IntoValue> fromValue(value: TValue): MarginImpl<M> {
            val dict = value as TDict<*>
            val top = ((dict["top"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into<M>()
            val bottom = ((dict["bottom"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into<M>()
            val inside = dict["inside"]
            val outside = dict["outside"]
            return if (inside != null || outside != null) {
                val left = ((inside ?: dict["x"] ?: dict["rest"]) as TValue?)?.into<M>()
                val right = ((outside ?: dict["x"] ?: dict["rest"]) as TValue?)?.into<M>()
                MarginImpl(SidesImpl(top, right, bottom, left), true.t)
            } else {
                val left = ((dict["left"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into<M>()
                val right = ((dict["right"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into<M>()
                MarginImpl(SidesImpl(top, right, bottom, left), false.t)
            }
        }

        fun fromValue(value: TValue, type: KType): MarginImpl<*> {
            val arg = type.arguments.firstOrNull()?.type
            val dict = value as TDict<*>
            val top = ((dict["top"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
            val bottom = ((dict["bottom"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
            val inside = dict["inside"]
            val outside = dict["outside"]
            return if (inside != null || outside != null) {
                val left = ((inside ?: dict["x"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
                val right = ((outside ?: dict["x"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
                MarginImpl(SidesImpl(top, right, bottom, left), true.t)
            } else {
                val left = ((dict["left"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
                val right = ((dict["right"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
                MarginImpl(SidesImpl(top, right, bottom, left), false.t)
            }
        }
    }
}

fun <M : IntoValue> Margin(top: M, bottom: M, left: M, right: M): Margin<M> =
    MarginImpl(Sides(top, right, bottom, left), false.t)


