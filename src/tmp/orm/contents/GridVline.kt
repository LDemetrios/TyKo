package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("grid.vline")
@Serializable
data class TGridVline(
    @SerialName("x") val x : TAutoOrInt? = null, 
    @SerialName("start") val start : TInt? = null, 
    @SerialName("end") val end : TIntOrNone? = null, 
    @SerialName("stroke") val stroke : TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null, 
    @SerialName("position") val position : TAlignment? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("grid.vline", ArgumentEntry(false, "x", x), ArgumentEntry(false, "start", start), ArgumentEntry(false, "end", end), ArgumentEntry(false, "stroke", stroke), ArgumentEntry(false, "position", position), )
}
