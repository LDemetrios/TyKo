package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TImageFormat(override val value: String) : TStr, 
    TAutoOrImageFormat {
    Png("png"),
    Jpg("jpg"),
    Gif("gif"),
    Svg("svg"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TImageFormat? = when(str) {
                    "png" -> TImageFormat.Png
            "jpg" -> TImageFormat.Jpg
            "gif" -> TImageFormat.Gif
            "svg" -> TImageFormat.Svg
            else -> null
        }
    }
}


