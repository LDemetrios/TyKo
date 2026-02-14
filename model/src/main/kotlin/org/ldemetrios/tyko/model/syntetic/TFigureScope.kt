package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.IntoStr
import org.ldemetrios.tyko.model.SerialName
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.t

/**
 * Lists figure scopes (used at [org.ldemetrios.tyko.model.TFigure.scope]).
 */
@SerialName("str")
enum class TFigureScope : IntoStr, Self<TFigureScope> {
    @SerialName("column") COLUMN, @SerialName("parent") PARENT;

    override fun intoValue(): TStr = name.lowercase().t

    companion object {
        private val valuesByStr by lazy { entries.associateBy { it.intoValue() } }
        fun fromValue(value: TValue) =
            if (value is TStr) valuesByStr[value]!! else throw AssertionError("Can't convert from $value")
    }
}