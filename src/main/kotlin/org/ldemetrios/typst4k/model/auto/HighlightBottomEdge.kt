package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class THighlightBottomEdge(override val value: String) : TStr, 
    THighlightBottomEdgeOrLength {
    Baseline("baseline"),
    Descender("descender"),
    Bounds("bounds"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : THighlightBottomEdge? = when(str) {
                    "baseline" -> THighlightBottomEdge.Baseline
            "descender" -> THighlightBottomEdge.Descender
            "bounds" -> THighlightBottomEdge.Bounds
            else -> null
        }
    }
}


