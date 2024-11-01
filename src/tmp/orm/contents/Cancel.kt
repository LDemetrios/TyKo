package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("cancel")
@Serializable
data class TCancel(
    @SerialName("body") val body : TContent, 
    @SerialName("length") val length : TRelative? = null, 
    @SerialName("inverted") val inverted : TBool? = null, 
    @SerialName("cross") val cross : TBool? = null, 
    @SerialName("angle") val angle : TAngleOrAuto? = null, 
    @SerialName("stroke") val stroke : TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<*, >? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.cancel", ArgumentEntry(false, null, body), ArgumentEntry(false, "length", length), ArgumentEntry(false, "inverted", inverted), ArgumentEntry(false, "cross", cross), ArgumentEntry(false, "angle", angle), ArgumentEntry(false, "stroke", stroke), )
}
