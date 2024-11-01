package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("path")
@Serializable
data class TPath(
    @SerialName("vertices") val vertices : TArray<TArray<*, >, >, 
    @SerialName("fill") val fill : TColorOrGradientOrNoneOrPattern? = null, 
    @SerialName("fill-rule") val fillRule : TStr? = null, 
    @SerialName("stroke") val stroke : TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null, 
    @SerialName("closed") val closed : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("path", ArgumentEntry(true, null, vertices), ArgumentEntry(false, "fill", fill), ArgumentEntry(false, "fill-rule", fillRule), ArgumentEntry(false, "stroke", stroke), ArgumentEntry(false, "closed", closed), )
}
