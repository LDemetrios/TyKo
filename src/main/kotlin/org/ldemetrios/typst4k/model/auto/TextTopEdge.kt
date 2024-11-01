package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TTextTopEdge(override val value: String) : TStr, 
    TLengthOrTextTopEdge {
    Ascender("ascender"),
    CapHeight("cap-height"),
    XHeight("x-height"),
    Baseline("baseline"),
    Bounds("bounds"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TTextTopEdge? = when(str) {
                    "ascender" -> TTextTopEdge.Ascender
            "cap-height" -> TTextTopEdge.CapHeight
            "x-height" -> TTextTopEdge.XHeight
            "baseline" -> TTextTopEdge.Baseline
            "bounds" -> TTextTopEdge.Bounds
            else -> null
        }
    }
}


