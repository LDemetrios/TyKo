package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("skew")
@Serializable
data class TSkew(
    @SerialName("body") val body : TContent, 
    @SerialName("ax") val ax : TAngle? = null, 
    @SerialName("ay") val ay : TAngle? = null, 
    @SerialName("origin") val origin : TAlignment? = null, 
    @SerialName("reflow") val reflow : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("skew", ArgumentEntry(false, null, body), ArgumentEntry(false, "ax", ax), ArgumentEntry(false, "ay", ay), ArgumentEntry(false, "origin", origin), ArgumentEntry(false, "reflow", reflow), )
}
