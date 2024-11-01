package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("underparen")
@Serializable
data class TUnderparen(
    @SerialName("body") val body : TContent, 
    @SerialName("annotation") val annotation : TContentOrNone? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.underparen", ArgumentEntry(false, null, body), ArgumentEntry(false, null, annotation), )
}
