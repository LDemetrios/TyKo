package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TMathClassClass(override val value: String) : TStr {
    Normal("normal"),
    Punctuation("punctuation"),
    Opening("opening"),
    Closing("closing"),
    Fence("fence"),
    Large("large"),
    Relation("relation"),
    Unary("unary"),
    Binary("binary"),
    Vary("vary"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TMathClassClass? = when(str) {
                    "normal" -> TMathClassClass.Normal
            "punctuation" -> TMathClassClass.Punctuation
            "opening" -> TMathClassClass.Opening
            "closing" -> TMathClassClass.Closing
            "fence" -> TMathClassClass.Fence
            "large" -> TMathClassClass.Large
            "relation" -> TMathClassClass.Relation
            "unary" -> TMathClassClass.Unary
            "binary" -> TMathClassClass.Binary
            "vary" -> TMathClassClass.Vary
            else -> null
        }
    }
}


