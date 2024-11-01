package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("strong")
@Serializable
data class TStrong(
    @SerialName("body") val body : TContent, 
    @SerialName("delta") val delta : TInt? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("strong", ArgumentEntry(false, null, body), ArgumentEntry(false, "delta", delta), )
}
