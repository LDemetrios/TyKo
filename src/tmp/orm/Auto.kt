package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("auto")
@Serializable
data object TAuto : TValue, 
    TAutoOrStr, 
    TArrayOrAuto<Nothing>, 
    TAutoOrContentOrNone, 
    TAlignmentOrAutoOrNone, 
    TAutoOrInt, 
    TAutoOrBool, 
    TAutoOrLength, 
    TAutoOrBoolOrNoneOrRelative, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TAlignmentOrAuto, 
    TAutoOrNoneOrStr, 
    TArrayOrAutoOrDictionaryOrStr<Nothing, Nothing>, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TAlignmentOrArrayOrAuto<Nothing>, 
    TAutoOrNone, 
    TAutoOrDirection, 
    TAutoOrFractionOrRelative, 
    TAutoOrDatetimeOrNone, 
    TAutoOrRelative, 
    TAngleOrAuto, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TAutoOrDictionaryOrRelative<Nothing>, 
    TAutoOrLengthOrRatio, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrGradientOrPattern, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>, 
    TAutoOrFloat, 
    TAutoOrContent
{
    override fun repr() : String = RT.structRepr("auto", )
}
