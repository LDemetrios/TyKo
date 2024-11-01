package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("line")
@Serializable
data class TLine(
    @SerialName("start") val start : TArray<*, >? = null, 
    @SerialName("end") val end : TArrayOrNone<*, >? = null, 
    @SerialName("length") val length : TRelative? = null, 
    @SerialName("angle") val angle : TAngle? = null, 
    @SerialName("stroke") val stroke : TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<*, >? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("line", ArgumentEntry(false, "start", start), ArgumentEntry(false, "end", end), ArgumentEntry(false, "length", length), ArgumentEntry(false, "angle", angle), ArgumentEntry(false, "stroke", stroke), )
}
