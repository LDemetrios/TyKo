package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

interface TFunction : TValue, 
    TArrayOrContentOrFunction<Nothing>, 
    TAutoOrFunctionOrStr, 
    TAutoOrContentOrFunctionOrNone, 
    TFunctionOrNoneOrStr, 
    TFunctionOrStr, 
    TFunctionOrLabelOrLocationOrSelector, 
    TAutoOrBoolOrFunctionOrNoneOrRelative, 
    TArrayOrColorOrFunctionOrGradientOrNoneOrPattern<Nothing>, 
    TAlignmentOrArrayOrAutoOrFunction<Nothing>, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, Nothing>, 
    TArrayOrFractionOrFunctionOrRelativeOrSides<Nothing, Nothing>, 
    TAngleOrAutoOrFunction, 
    TArrayOrDictionaryOrFunctionOrRelative<Nothing, Nothing>, 
    TFunctionOrLabelOrLocationOrSelectorOrStr, 
    TArrayOrFunctionOrInt<Nothing>{
    override fun type() : TType = TFunction
    override fun format() = Representations.reprOf(this)
    companion object: TType("function") {
    }   
}
