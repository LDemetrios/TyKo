package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("repeat")
@Serializable
data class TRepeat(
    @SerialName("body") val body : TContent, 
    @SerialName("gap") val gap : TLength? = null, 
    @SerialName("justify") val justify : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("repeat", ArgumentEntry(false, null, body), ArgumentEntry(false, "gap", gap), ArgumentEntry(false, "justify", justify), )
}
