package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("enum.item")
@Serializable
data class TEnumItem(
    @SerialName("number") val number : TIntOrNone? = null, 
    @SerialName("body") val body : TContent, 
) : TContent
{
    override fun repr() : String = RT.structRepr("enum.item", ArgumentEntry(false, null, number), ArgumentEntry(false, null, body), )
}
