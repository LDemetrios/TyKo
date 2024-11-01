package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("lr")
@Serializable
data class TLr(
    @SerialName("body") val body : TContent, 
    @SerialName("size") val size : TAutoOrRelative? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.lr", ArgumentEntry(false, null, body), ArgumentEntry(false, "size", size), )
}
