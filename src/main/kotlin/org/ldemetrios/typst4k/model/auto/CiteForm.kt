package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TCiteForm(override val value: String) : TStr, 
    TCiteFormOrNone {
    Normal("normal"),
    Prose("prose"),
    Full("full"),
    Author("author"),
    Year("year"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TCiteForm? = when(str) {
                    "normal" -> TCiteForm.Normal
            "prose" -> TCiteForm.Prose
            "full" -> TCiteForm.Full
            "author" -> TCiteForm.Author
            "year" -> TCiteForm.Year
            else -> null
        }
    }
}


