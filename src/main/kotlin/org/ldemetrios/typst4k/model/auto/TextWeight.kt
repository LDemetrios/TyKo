package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TTextWeight(override val value: String) : TStr, 
    TIntOrTextWeight {
    Thin("thin"),
    Extralight("extralight"),
    Light("light"),
    Regular("regular"),
    Medium("medium"),
    Semibold("semibold"),
    Bold("bold"),
    Extrabold("extrabold"),
    Black("black"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TTextWeight? = when(str) {
                    "thin" -> TTextWeight.Thin
            "extralight" -> TTextWeight.Extralight
            "light" -> TTextWeight.Light
            "regular" -> TTextWeight.Regular
            "medium" -> TTextWeight.Medium
            "semibold" -> TTextWeight.Semibold
            "bold" -> TTextWeight.Bold
            "extrabold" -> TTextWeight.Extrabold
            "black" -> TTextWeight.Black
            else -> null
        }
    }
}


