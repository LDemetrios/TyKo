package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("terms.item")
@Serializable
data class TTermsItem(
    @SerialName("term") val term : TContent, 
    @SerialName("description") val description : TContent, 
) : TContent
{
    override fun repr() : String = RT.structRepr("terms.item", ArgumentEntry(false, null, term), ArgumentEntry(false, null, description), )
}
