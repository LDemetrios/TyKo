package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TParLinebreaks(override val value: String) : TStr, 
    TAutoOrParLinebreaks {
    Simple("simple"),
    Optimized("optimized"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TParLinebreaks? = when(str) {
                    "simple" -> TParLinebreaks.Simple
            "optimized" -> TParLinebreaks.Optimized
            else -> null
        }
    }
}


