package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("highlight")
@Serializable
data class THighlight(
    @SerialName("body") val body : TContent, 
    @SerialName("fill") val fill : TColorOrGradientOrPattern? = null, 
    @SerialName("stroke") val stroke : TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null, 
    @SerialName("top-edge") val topEdge : TLengthOrStr? = null, 
    @SerialName("bottom-edge") val bottomEdge : TLengthOrStr? = null, 
    @SerialName("extent") val extent : TLength? = null, 
    @SerialName("radius") val radius : TDictionaryOrRelative<*, >? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("highlight", ArgumentEntry(false, null, body), ArgumentEntry(false, "fill", fill), ArgumentEntry(false, "stroke", stroke), ArgumentEntry(false, "top-edge", topEdge), ArgumentEntry(false, "bottom-edge", bottomEdge), ArgumentEntry(false, "extent", extent), ArgumentEntry(false, "radius", radius), )
}
