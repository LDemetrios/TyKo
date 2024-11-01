package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("align")
@Serializable
data class TAlign(
    @SerialName("alignment") val alignment : TAlignment, 
    @SerialName("body") val body : TContent, 
) : TContent
{
    override fun repr() : String = RT.structRepr("align", ArgumentEntry(false, null, alignment), ArgumentEntry(false, null, body), )
}
