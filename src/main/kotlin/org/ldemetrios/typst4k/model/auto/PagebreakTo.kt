package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TPagebreakTo(override val value: String) : TStr, 
    TNoneOrPagebreakTo {
    Even("even"),
    Odd("odd"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TPagebreakTo? = when(str) {
                    "even" -> TPagebreakTo.Even
            "odd" -> TPagebreakTo.Odd
            else -> null
        }
    }
}


