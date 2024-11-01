package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TInt(val value: Long)
 :  TValue, 
    TAutoOrInt, 
    TIntOrNone, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TAutoOrFractionOrIntOrRelative, 
    TIntOrTextWeight, 
    TArrayOrIntOrNone<Nothing>, 
    TDictionaryOrIntOrNone<Nothing>, 
    TIntOrRatio, 
    TArrayOrFunctionOrInt<Nothing>, 
    TIntOrLength {
    override fun type() : TType = TInt
    override fun format() = Representations.reprOf(value)
    
    companion object : TType("int") {
    }
}
