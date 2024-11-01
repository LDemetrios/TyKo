package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("scale")
@Serializable
data class TScale(
    @SerialName("body") val body : TContent, 
    @SerialName("x") val x : TAutoOrLengthOrRatio? = null, 
    @SerialName("y") val y : TAutoOrLengthOrRatio? = null, 
    @SerialName("origin") val origin : TAlignment? = null, 
    @SerialName("reflow") val reflow : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("scale", ArgumentEntry(false, null, body), ArgumentEntry(false, "x", x), ArgumentEntry(false, "y", y), ArgumentEntry(false, "origin", origin), ArgumentEntry(false, "reflow", reflow), )
}
