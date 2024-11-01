package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("undershell")
@Serializable
data class TUndershell(
    @SerialName("body") val body : TContent, 
    @SerialName("annotation") val annotation : TContentOrNone? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.undershell", ArgumentEntry(false, null, body), ArgumentEntry(false, null, annotation), )
}
