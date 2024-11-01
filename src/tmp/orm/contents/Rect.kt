package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("rect")
@Serializable
data class TRect(
    @SerialName("body") val body : TContentOrNone? = null, 
    @SerialName("width") val width : TAutoOrFractionOrRelative? = null, 
    @SerialName("height") val height : TAutoOrFractionOrRelative? = null, 
    @SerialName("fill") val fill : TColorOrGradientOrNoneOrPattern? = null, 
    @SerialName("stroke") val stroke : TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null, 
    @SerialName("radius") val radius : TDictionaryOrRelative<*, >? = null, 
    @SerialName("inset") val inset : TDictionaryOrRelative<*, >? = null, 
    @SerialName("outset") val outset : TDictionaryOrRelative<*, >? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("rect", ArgumentEntry(false, null, body), ArgumentEntry(false, "width", width), ArgumentEntry(false, "height", height), ArgumentEntry(false, "fill", fill), ArgumentEntry(false, "stroke", stroke), ArgumentEntry(false, "radius", radius), ArgumentEntry(false, "inset", inset), ArgumentEntry(false, "outset", outset), )
}
