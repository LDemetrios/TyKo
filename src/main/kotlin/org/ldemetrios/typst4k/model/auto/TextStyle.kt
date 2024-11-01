package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TTextStyle(override val value: String) : TStr {
    Normal("normal"),
    Italic("italic"),
    Oblique("oblique"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TTextStyle? = when(str) {
                    "normal" -> TTextStyle.Normal
            "italic" -> TTextStyle.Italic
            "oblique" -> TTextStyle.Oblique
            else -> null
        }
    }
}


