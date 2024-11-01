package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("move")
@Serializable
data class TMove(
    @SerialName("body") val body : TContent, 
    @SerialName("dx") val dx : TRelative? = null, 
    @SerialName("dy") val dy : TRelative? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("move", ArgumentEntry(false, null, body), ArgumentEntry(false, "dx", dx), ArgumentEntry(false, "dy", dy), )
}
