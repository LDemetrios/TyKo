package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class THighlightTopEdge(override val value: String) : TStr, 
    THighlightTopEdgeOrLength {
    Ascender("ascender"),
    CapHeight("cap-height"),
    XHeight("x-height"),
    Baseline("baseline"),
    Bounds("bounds"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : THighlightTopEdge? = when(str) {
                    "ascender" -> THighlightTopEdge.Ascender
            "cap-height" -> THighlightTopEdge.CapHeight
            "x-height" -> THighlightTopEdge.XHeight
            "baseline" -> THighlightTopEdge.Baseline
            "bounds" -> THighlightTopEdge.Bounds
            else -> null
        }
    }
}


