package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.IntoStr
import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.SerialName
import org.ldemetrios.tyko.model.TLength
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TValue


sealed interface TopEdge : IntoValue {
    companion object {
        fun fromValue(value: TValue) = when (value) {
            is TLength -> value
            is TStr -> TopEdgePreset.fromValue(value)
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

sealed interface BottomEdge : IntoValue {
    companion object {
        fun fromValue(value: TValue) = when (value) {
            is TLength -> value
            is TStr -> BottomEdgePreset.fromValue(value)
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

/**
 * Lists built-in top edge presets for text metrics alignment.
 */
@SerialName("str")
enum class TopEdgePreset(val value: String) : IntoStr, TopEdge, Self<TopEdgePreset> {
    ASCENDER("ascender"),
    CAP_HEIGHT("cap-height"),
    X_HEIGHT("x-height"),
    BASELINE("baseline"),
    BOUNDS("bounds");

    override fun intoValue(): TStr = TStr(value)

    companion object {
        fun fromValue(value: TValue): TopEdgePreset {
            val str = (value as TStr).value
            return entries.first { it.value == str }
        }
    }
}

/**
 * Lists built-in bottom edge presets for text metrics alignment.
 */
@SerialName("str")
enum class BottomEdgePreset(val value: String) : IntoStr, BottomEdge, Self<BottomEdgePreset> {
    DESCENDER("descender"),
    BASELINE("baseline"),
    BOUNDS("bounds");

    override fun intoValue(): TStr = TStr(value)

    companion object {
        fun fromValue(value: TValue): BottomEdgePreset {
            val str = (value as TStr).value
            return entries.first { it.value == str }
        }
    }
}
