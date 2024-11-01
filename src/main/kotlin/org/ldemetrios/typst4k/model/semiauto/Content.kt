package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

abstract class TContent : TValue, 
    TAutoOrContentOrNone, 
    TArrayOrContentOrFunction<Nothing>, 
    TContentOrNone, 
    TAutoOrContentOrFunctionOrNone, 
    TAutoOrContent, 
    TContentOrLabel, 
    TContentOrLabelOrNone, 
    TArrayOrContent<Nothing>, 
    TContentOrStr, 
    TContentOrFractionOrRelative{
    abstract val label: TLabel?
    abstract fun func(): TElement
    override fun type(): TType = TContent
    companion object : TType("content") {
    }
}
