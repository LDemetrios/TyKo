package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data object TNone : TValue, 
    TAutoOrContentOrNone, 
    TContentOrNone, 
    TCiteFormOrNone, 
    TAutoOrDatetimeOrNone, 
    TAlignmentOrAutoOrNone, 
    TAutoOrContentOrFunctionOrNone, 
    TFunctionOrNoneOrStr, 
    TIntOrNone, 
    TAutoOrBoolOrFunctionOrNoneOrRelative, 
    TContentOrLabelOrNone, 
    TArrayOrColorOrFunctionOrGradientOrNoneOrPattern<Nothing>, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, Nothing>, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TColorOrGradientOrNoneOrPattern, 
    TNoneOrStr, 
    TAutoOrNoneOrStr, 
    TAutoOrNone, 
    TArrayOrIntOrNone<Nothing>, 
    TArrayOrNoneOrStr<Nothing>, 
    TDictionaryOrIntOrNone<Nothing>, 
    TNoneOrPagebreakTo, 
    TFractionOrNoneOrRelative, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TArrayOrNone<Nothing>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>, 
    TNoneOrSelector {
    override fun format() = Representations.structRepr(
		"none",
	)
	override fun type(): TType = TNoneType
}
data object TNoneType : TType("none")
