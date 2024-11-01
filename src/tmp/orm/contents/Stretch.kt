package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("stretch")
@Serializable
data class TStretch(
    @SerialName("body") val body : TContent, 
    @SerialName("size") val size : TAutoOrRelative? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.stretch", ArgumentEntry(false, null, body), ArgumentEntry(false, "size", size), )
}
