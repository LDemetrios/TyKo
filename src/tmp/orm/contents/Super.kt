package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("super")
@Serializable
data class TSuper(
    @SerialName("body") val body : TContent, 
    @SerialName("typographic") val typographic : TBool? = null, 
    @SerialName("baseline") val baseline : TLength? = null, 
    @SerialName("size") val size : TLength? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("super", ArgumentEntry(false, null, body), ArgumentEntry(false, "typographic", typographic), ArgumentEntry(false, "baseline", baseline), ArgumentEntry(false, "size", size), )
}
