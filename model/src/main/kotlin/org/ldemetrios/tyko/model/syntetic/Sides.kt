package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.ArrayOrSingle
import org.ldemetrios.tyko.model.IntoDict
import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.Option
import org.ldemetrios.tyko.model.SerialName
import org.ldemetrios.tyko.model.Sides
import org.ldemetrios.tyko.model.Smart
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.into
import org.ldemetrios.tyko.model.mapOfNotNullValues
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Defines the synthetic dictionary type with specific keys.
 * See usage at https://typst.app/docs/reference/visualize/rect/#parameters-stroke.
 */
sealed interface Sides<out S : IntoValue> : IntoValue {
    val top: S?
    val right: S?
    val bottom: S?
    val left: S?

    companion object {
        inline fun <reified S : IntoValue> fromValue(value: TValue): Sides<S> = when (value) {
            is TDict<*> -> SidesImpl.fromValue(value)
            is S -> value as SidesSplat<S>
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): Sides<*> = when (value) {
            is TDict<*> -> SidesImpl.fromValue(value, type)
            else -> {
                val arg = type.arguments.firstOrNull()?.type
                if (arg == null) value as SidesSplat<*>
                else value.into(arg) as SidesSplat<*>
            }
        }
    }
}


sealed interface SidesSplat<out Self : SidesSplat<Self>> : Sides<Self> {
    override val top: Self get() = this as Self
    override val right: Self get() = this as Self
    override val bottom: Self get() = this as Self
    override val left: Self get() = this as Self
}

@SerialName("dict")
data class SidesImpl<out S : IntoValue>(
    override val top: S?,
    override val right: S?,
    override val bottom: S?,
    override val left: S?,
) : IntoValue,
    IntoDict<S>, Sides<S>, ArrayOrSingle<SidesImpl<S>>, Option<SidesImpl<S>>, Smart<SidesImpl<S>> {
    override fun intoValue(): TDict<S> = TDict(
        mapOfNotNullValues(
            "top" to top,
            "right" to right,
            "bottom" to bottom,
            "left" to left,
        )
    )

    companion object {
        fun <T : IntoValue> splat(x: T) = Sides(x, x, x, x)

        inline fun <reified T : IntoValue> fromValue(value: TValue): SidesImpl<T> {
            val dict = value as TDict<*>
            val top = ((dict["top"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into<T>()
            val right = ((dict["right"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into<T>()
            val bottom = ((dict["bottom"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into<T>()
            val left = ((dict["left"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into<T>()
            return SidesImpl(top, right, bottom, left)
        }

        fun fromValue(value: TValue, type: KType): SidesImpl<*> {
            val arg = type.arguments.firstOrNull()?.type
            val dict = value as TDict<*>
            val top = ((dict["top"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
            val right = ((dict["right"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
            val bottom = ((dict["bottom"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
            val left = ((dict["left"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
            return SidesImpl(top, right, bottom, left)
        }
    }
}

fun <S : IntoValue> Sides(top: S, right: S, bottom: S, left: S) = SidesImpl(top, right, bottom, left)
