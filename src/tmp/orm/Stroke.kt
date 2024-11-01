package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("stroke")
@Serializable
data class TStroke(
    @SerialName("paint") val paint : TAutoOrColorOrGradientOrPattern? = null, 
    @SerialName("thickness") val thickness : TAutoOrLength? = null, 
    @SerialName("cap") val cap : TAutoOrStr? = null, 
    @SerialName("join") val join : TAutoOrStr? = null, 
    @SerialName("dash") val dash : TArrayOrAutoOrDictionaryOrNoneOrStr<*, *, >? = null, 
    @SerialName("miter-limit") val miterLimit : TAutoOrFloat? = null, 
) : TValue, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, Nothing>, 
    TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>
{
    override fun repr() : String = RT.structRepr("stroke", ArgumentEntry(false, "paint", paint), ArgumentEntry(false, "thickness", thickness), ArgumentEntry(false, "cap", cap), ArgumentEntry(false, "join", join), ArgumentEntry(false, "dash", dash), ArgumentEntry(false, "miter-limit", miterLimit), )
}
