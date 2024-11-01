package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TArray<out E : TValue>(val value: List<E>)
 : List<E> by value, TValue, 
    TArrayOrStr<E>, 
    TArrayOrContentOrFunction<E>, 
    TArrayOrEnumItem<E>, 
    TArrayOrAutoOrFractionOrIntOrRelative<E>, 
    TArrayOrColorOrFunctionOrGradientOrNoneOrPattern<E>, 
    TAlignmentOrArrayOrAutoOrFunction<E>, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<E, Nothing>, 
    TArrayOrFractionOrFunctionOrRelativeOrSides<E, Nothing>, 
    TArrayOrContent<E>, 
    TArrayOrAutoOrDictionaryOrStr<E, Nothing>, 
    TArrayOrIntOrNone<E>, 
    TArrayOrDictionary<E, Nothing>, 
    TArrayOrNoneOrStr<E>, 
    TArrayOrDictionaryOrFunctionOrRelative<E, Nothing>, 
    TArrayOrNone<E>, 
    TArrayOrColor<E>, 
    TArrayOrAuto<E>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, Nothing>, 
    TArrayOrFunctionOrInt<E> {
    override fun type() : TType = TArray
    override fun format() = Representations.reprOf(value)
    
    companion object : TType("array") {
    }
}
