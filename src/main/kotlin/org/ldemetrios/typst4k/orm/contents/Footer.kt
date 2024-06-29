package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("grid.footer")
@Serializable
data class TFooter(
    @SerialName("children") val children : TArray<TContent, >, 
    @SerialName("repeat") val repeat : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("grid.footer", Triple(false, null, children), Triple(false, "repeat", repeat), )
}
