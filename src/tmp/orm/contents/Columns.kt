package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("columns")
@Serializable
data class TColumns(
    @SerialName("count") val count : TInt? = null, 
    @SerialName("body") val body : TContent, 
    @SerialName("gutter") val gutter : TRelative? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("columns", ArgumentEntry(false, null, count), ArgumentEntry(false, null, body), ArgumentEntry(false, "gutter", gutter), )
}
