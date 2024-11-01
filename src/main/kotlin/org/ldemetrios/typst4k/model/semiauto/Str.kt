package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

interface TStr : TValue, 
    TArrayOrStr<Nothing>, 
    TAutoOrFunctionOrStr, 
    TFunctionOrNoneOrStr, 
    TFunctionOrStr, 
    TLabelOrLocationOrPositionOrStr, 
    TNoneOrStr, 
    TAutoOrNoneOrStr, 
    TArrayOrAutoOrDictionaryOrStr<Nothing, Nothing>, 
    TAutoOrStr, 
    TContentOrStr, 
    TArrayOrNoneOrStr<Nothing>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>, 
    TFunctionOrLabelOrLocationOrSelectorOrStr{
    override fun type() : TType = TStr
    abstract val value: String
    override fun format() = Representations.reprOf(this)
    companion object: TType("str") {
    }   
}
