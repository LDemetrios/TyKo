package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("class")
@Serializable
data class TClass(
    @SerialName("class") val clazz : TStr, 
    @SerialName("body") val body : TContent, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.class", ArgumentEntry(false, null, clazz), ArgumentEntry(false, null, body), )
}
