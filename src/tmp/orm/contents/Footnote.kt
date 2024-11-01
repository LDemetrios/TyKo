package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("footnote")
@Serializable
data class TFootnote(
    @SerialName("body") val body : TContentOrLabel, 
    @SerialName("numbering") val numbering : TStr? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("footnote", ArgumentEntry(false, null, body), ArgumentEntry(false, "numbering", numbering), )
}
