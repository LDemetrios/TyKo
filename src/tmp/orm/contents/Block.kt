package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("block")
@Serializable
data class TBlock(
    @SerialName("body") val body : TContentOrNone? = null, 
    @SerialName("width") val width : TAutoOrFractionOrRelative? = null, 
    @SerialName("height") val height : TAutoOrFractionOrRelative? = null, 
    @SerialName("breakable") val breakable : TBool? = null, 
    @SerialName("fill") val fill : TColorOrGradientOrNoneOrPattern? = null, 
    @SerialName("stroke") val stroke : TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null, 
    @SerialName("radius") val radius : TDictionaryOrRelative<*, >? = null, 
    @SerialName("inset") val inset : TDictionaryOrRelative<*, >? = null, 
    @SerialName("outset") val outset : TDictionaryOrRelative<*, >? = null, 
    @SerialName("spacing") val spacing : TFractionOrRelative? = null, 
    @SerialName("above") val above : TFractionOrRelative? = null, 
    @SerialName("below") val below : TFractionOrRelative? = null, 
    @SerialName("clip") val clip : TBool? = null, 
    @SerialName("sticky") val sticky : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("block", ArgumentEntry(false, null, body), ArgumentEntry(false, "width", width), ArgumentEntry(false, "height", height), ArgumentEntry(false, "breakable", breakable), ArgumentEntry(false, "fill", fill), ArgumentEntry(false, "stroke", stroke), ArgumentEntry(false, "radius", radius), ArgumentEntry(false, "inset", inset), ArgumentEntry(false, "outset", outset), ArgumentEntry(false, "spacing", spacing), ArgumentEntry(false, "above", above), ArgumentEntry(false, "below", below), ArgumentEntry(false, "clip", clip), ArgumentEntry(false, "sticky", sticky), )
}
