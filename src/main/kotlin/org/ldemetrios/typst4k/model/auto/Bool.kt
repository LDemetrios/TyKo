package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TBool(val value: Boolean)
 :  TValue, 
    TAutoOrBool, 
    TAutoOrBoolOrFunctionOrNoneOrRelative {
    override fun type() : TType = TBool
    override fun format() = Representations.reprOf(value)
    
    companion object : TType("bool") {
    }
}
