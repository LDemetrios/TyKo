package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TTextBottomEdge(override val value: String) : TStr, 
    TLengthOrTextBottomEdge {
    Baseline("baseline"),
    Descender("descender"),
    Bounds("bounds"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TTextBottomEdge? = when(str) {
                    "baseline" -> TTextBottomEdge.Baseline
            "descender" -> TTextBottomEdge.Descender
            "bounds" -> TTextBottomEdge.Bounds
            else -> null
        }
    }
}


