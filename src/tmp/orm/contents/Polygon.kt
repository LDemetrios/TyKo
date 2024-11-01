package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("polygon")
@Serializable
data class TPolygon(
    @SerialName("vertices") val vertices : TArray<TArray<TRelative, >, >, 
    @SerialName("fill") val fill : TColorOrGradientOrNoneOrPattern? = null, 
    @SerialName("fill-rule") val fillRule : TStr? = null, 
    @SerialName("stroke") val stroke : TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("polygon", ArgumentEntry(true, null, vertices), ArgumentEntry(false, "fill", fill), ArgumentEntry(false, "fill-rule", fillRule), ArgumentEntry(false, "stroke", stroke), )
}
