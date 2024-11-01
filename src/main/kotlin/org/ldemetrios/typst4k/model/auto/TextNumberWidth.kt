package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TTextNumberWidth(override val value: String) : TStr, 
    TAutoOrTextNumberWidth {
    Proportional("proportional"),
    Tabular("tabular"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TTextNumberWidth? = when(str) {
                    "proportional" -> TTextNumberWidth.Proportional
            "tabular" -> TTextNumberWidth.Tabular
            else -> null
        }
    }
}


