package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("link")
@Serializable
data class TLink(
    @SerialName("dest") val dest : TDictionaryOrLabelOrLocationOrStr<TIntOrLength, >, 
    @SerialName("body") val body : TContent, 
) : TContent
{
    override fun repr() : String = RT.structRepr("link", ArgumentEntry(false, null, dest), ArgumentEntry(false, null, body), )
}
