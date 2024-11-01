package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("box")
@Serializable
data class TBox(
    @SerialName("body") val body : TContentOrNone? = null, 
    @SerialName("width") val width : TAutoOrFractionOrRelative? = null, 
    @SerialName("height") val height : TAutoOrFractionOrRelative? = null, 
    @SerialName("baseline") val baseline : TRelative? = null, 
    @SerialName("fill") val fill : TColorOrGradientOrNoneOrPattern? = null, 
    @SerialName("stroke") val stroke : TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null, 
    @SerialName("radius") val radius : TDictionaryOrRelative<*, >? = null, 
    @SerialName("inset") val inset : TDictionaryOrRelative<*, >? = null, 
    @SerialName("outset") val outset : TDictionaryOrRelative<*, >? = null, 
    @SerialName("clip") val clip : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("box", ArgumentEntry(false, null, body), ArgumentEntry(false, "width", width), ArgumentEntry(false, "height", height), ArgumentEntry(false, "baseline", baseline), ArgumentEntry(false, "fill", fill), ArgumentEntry(false, "stroke", stroke), ArgumentEntry(false, "radius", radius), ArgumentEntry(false, "inset", inset), ArgumentEntry(false, "outset", outset), ArgumentEntry(false, "clip", clip), )
}
