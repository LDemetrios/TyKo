package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.IntoStr
import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.SerialName
import org.ldemetrios.tyko.model.TFontDescriptorImpl
import org.ldemetrios.tyko.model.TRegex
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.t

sealed interface TFontCoverage : IntoValue {
    companion object {
        fun fromValue(value: TValue) : TFontCoverage = when (value) {
            is TRegex -> value
            is TStr -> TFontCoveragePreset.fromValue(value)
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

/**
 * Lists built-in font coverage presets (used at [TFontDescriptorImpl.covers]).
 */
@SerialName("str")
enum class TFontCoveragePreset(val value: String) : TFontCoverage, IntoStr, Self<TFontCoveragePreset> {
    LATIN_IN_CJK("latin-in-cjk");

    override fun intoValue(): TStr = value.t

    companion object {
        fun fromValue(value: TValue) = when (value) {
            LATIN_IN_CJK.value.t -> LATIN_IN_CJK
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}
