package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("figure.caption")
@Serializable
data class TCaption(
    @SerialName("body") val body : TContent, 
    @SerialName("position") val position : TAlignment? = null, 
    @SerialName("separator") val separator : TAutoOrContent? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("figure.caption", ArgumentEntry(false, null, body), ArgumentEntry(false, "position", position), ArgumentEntry(false, "separator", separator), )
}
