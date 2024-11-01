package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("grid.header")
@Serializable
data class TGridHeader(
    @SerialName("children") val children : TArray<TContent, >, 
    @SerialName("repeat") val repeat : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("grid.header", ArgumentEntry(true, null, children), ArgumentEntry(false, "repeat", repeat), )
}
