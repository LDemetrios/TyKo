package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("grid.hline")
@Serializable
data class TGridHline(
    @SerialName("y") val y : TAutoOrInt? = null, 
    @SerialName("start") val start : TInt? = null, 
    @SerialName("end") val end : TIntOrNone? = null, 
    @SerialName("stroke") val stroke : TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null, 
    @SerialName("position") val position : TAlignment? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("grid.hline", ArgumentEntry(false, "y", y), ArgumentEntry(false, "start", start), ArgumentEntry(false, "end", end), ArgumentEntry(false, "stroke", stroke), ArgumentEntry(false, "position", position), )
}
