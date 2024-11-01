package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("none")
@Serializable
data object TNone : TValue, 
    TAutoOrContentOrNone, 
    TContentOrNone, 
    TNoneOrStr, 
    TAlignmentOrAutoOrNone, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TIntOrNone, 
    TAutoOrBoolOrNoneOrRelative, 
    TContentOrLabelOrNone, 
    TAutoOrNoneOrStr, 
    TArrayOrColorOrGradientOrNoneOrPattern<Nothing>, 
    TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, Nothing>, 
    TAutoOrNone, 
    TArrayOrIntOrNone<Nothing>, 
    TAutoOrDatetimeOrNone, 
    TArrayOrNoneOrStr<Nothing>, 
    TDictionaryOrIntOrNone<Nothing>, 
    TColorOrGradientOrNoneOrPattern, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TFractionOrNoneOrRelative, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TArrayOrNone<Nothing>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>
{
    override fun repr() : String = RT.structRepr("none", )
}
