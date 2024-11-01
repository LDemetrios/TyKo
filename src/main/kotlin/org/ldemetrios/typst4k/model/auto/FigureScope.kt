package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

enum class TFigureScope(override val value: String) : TStr {
    Column("column"),
    Parent("parent"),
    ;
    companion object {
        fun of(str: TStr) = of(str.value)
        
        fun of(str: String) : TFigureScope? = when(str) {
                    "column" -> TFigureScope.Column
            "parent" -> TFigureScope.Parent
            else -> null
        }
    }
}


