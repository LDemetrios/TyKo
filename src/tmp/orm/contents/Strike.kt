package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("strike")
@Serializable
data class TStrike(
    @SerialName("body") val body : TContent, 
    @SerialName("stroke") val stroke : TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<*, >? = null, 
    @SerialName("offset") val offset : TAutoOrLength? = null, 
    @SerialName("extent") val extent : TLength? = null, 
    @SerialName("background") val background : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("strike", ArgumentEntry(false, null, body), ArgumentEntry(false, "stroke", stroke), ArgumentEntry(false, "offset", offset), ArgumentEntry(false, "extent", extent), ArgumentEntry(false, "background", background), )
}
