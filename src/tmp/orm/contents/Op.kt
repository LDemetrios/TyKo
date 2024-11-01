package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("op")
@Serializable
data class TOp(
    @SerialName("text") val text : TContent, 
    @SerialName("limits") val limits : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.op", ArgumentEntry(false, null, text), ArgumentEntry(false, "limits", limits), )
}
