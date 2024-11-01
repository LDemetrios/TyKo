package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("ref")
@Serializable
data class TRef(
    @SerialName("target") val target : TLabel, 
    @SerialName("supplement") val supplement : TAutoOrContentOrNone? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("ref", ArgumentEntry(false, null, target), ArgumentEntry(false, "supplement", supplement), )
}
