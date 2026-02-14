package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.TInt
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TTextWeightPreset
import org.ldemetrios.tyko.model.TValue

sealed interface TTextWeight : IntoValue {
    companion object {
        inline fun fromValue(value: TValue): TTextWeight = when (value) {
            is TInt -> value
            is TStr -> TTextWeightPreset.fromValue(value)
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}


/**
 * Lists built-in text weight presets.
 */
@SerialName("str")
enum class TTextWeightPreset(val value: String) : IntoStr, TTextWeight, Self<TTextWeightPreset> {
    THIN("thin"),
    EXTRALIGHT("extralight"),
    LIGHT("light"),
    REGULAR("regular"),
    MEDIUM("medium"),
    SEMIBOLD("semibold"),
    BOLD("bold"),
    EXTRABOLD("extrabold"),
    BLACK("black");

    override fun intoValue(): TStr = TStr(value)

    companion object {
        fun fromValue(value: TValue): TTextWeightPreset {
            val str = (value as TStr).value
            return entries.first { it.value == str }
        }
    }
}
