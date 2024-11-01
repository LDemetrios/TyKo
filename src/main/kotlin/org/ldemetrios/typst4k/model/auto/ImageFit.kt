package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TImageFit(override val value: String) : TStr {
    Cover("cover"),
    Contain("contain"),
    Stretch("stretch"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TImageFit? = when(str) {
                    "cover" -> TImageFit.Cover
            "contain" -> TImageFit.Contain
            "stretch" -> TImageFit.Stretch
            else -> null
        }
    }
}


