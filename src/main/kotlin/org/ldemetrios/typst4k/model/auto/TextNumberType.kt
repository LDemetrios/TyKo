package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TTextNumberType(override val value: String) : TStr, 
    TAutoOrTextNumberType {
    Lining("lining"),
    OldStyle("old-style"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TTextNumberType? = when(str) {
                    "lining" -> TTextNumberType.Lining
            "old-style" -> TTextNumberType.OldStyle
            else -> null
        }
    }
}


