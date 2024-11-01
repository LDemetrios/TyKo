package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data object TAuto : TValue, 
    TAutoOrContentOrNone, 
    TAutoOrLength, 
    TAutoOrCiteStyle, 
    TAutoOrDatetimeOrNone, 
    TAlignmentOrAutoOrNone, 
    TAutoOrFunctionOrStr, 
    TAutoOrContentOrFunctionOrNone, 
    TAutoOrContent, 
    TAutoOrInt, 
    TAutoOrBool, 
    TAutoOrBoolOrFunctionOrNoneOrRelative, 
    TAutoOrParLinebreaks, 
    TAlignmentOrAuto, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TAutoOrFractionOrIntOrRelative, 
    TAlignmentOrArrayOrAutoOrFunction<Nothing>, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TAutoOrRelativeOrSides<Nothing>, 
    TAutoOrRelative, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TAutoOrNoneOrStr, 
    TArrayOrAutoOrDictionaryOrStr<Nothing, Nothing>, 
    TAutoOrNone, 
    TAutoOrStr, 
    TAutoOrDirection, 
    TAutoOrTextNumberType, 
    TAutoOrTextNumberWidth, 
    TAngleOrAutoOrFunction, 
    TAutoOrFractionOrRelative, 
    TAutoOrDictionaryOrRelative<Nothing>, 
    TAutoOrLengthOrRatio, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TAutoOrImageFormat, 
    TArrayOrAuto<Nothing>, 
    TAutoOrColorOrGradientOrPattern, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>, 
    TAutoOrFloat {
    override fun format() = Representations.structRepr(
		"auto",
	)
	override fun type(): TType = TAutoType
}
data object TAutoType : TType("auto")
