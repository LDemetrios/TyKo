package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("pattern")
@Serializable
data class TPattern(
    @SerialName("size") val size : TArrayOrAuto<*, >? = null, 
    @SerialName("spacing") val spacing : TArray<TLength, >? = null, 
    @SerialName("relative") val relative : TAutoOrStr? = null, 
    @SerialName("body") val body : TContent? = null, 
) : TValue, 
    TColorOrGradientOrPattern, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TArrayOrColorOrGradientOrNoneOrPattern<Nothing>, 
    TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, Nothing>, 
    TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TColorOrGradientOrNoneOrPattern, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrGradientOrPattern
{
    override fun repr() : String = RT.reprOf(this)
}
