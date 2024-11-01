package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("pad")
@Serializable
data class TPad(
    @SerialName("body") val body : TContent, 
    @SerialName("left") val left : TRelative? = null, 
    @SerialName("top") val top : TRelative? = null, 
    @SerialName("right") val right : TRelative? = null, 
    @SerialName("bottom") val bottom : TRelative? = null, 
    @SerialName("x") val x : TRelative? = null, 
    @SerialName("y") val y : TRelative? = null, 
    @SerialName("rest") val rest : TRelative? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("pad", ArgumentEntry(false, null, body), ArgumentEntry(false, "left", left), ArgumentEntry(false, "top", top), ArgumentEntry(false, "right", right), ArgumentEntry(false, "bottom", bottom), ArgumentEntry(false, "x", x), ArgumentEntry(false, "y", y), ArgumentEntry(false, "rest", rest), )
}
