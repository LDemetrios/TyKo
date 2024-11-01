package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TPolygonFillRule(override val value: String) : TStr {
    NonZero("non-zero"),
    EvenOdd("even-odd"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TPolygonFillRule? = when(str) {
                    "non-zero" -> TPolygonFillRule.NonZero
            "even-odd" -> TPolygonFillRule.EvenOdd
            else -> null
        }
    }
}


