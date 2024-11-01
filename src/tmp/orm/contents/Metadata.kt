package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("metadata")
@Serializable
data class TMetadata<out D : TValue>(
    @SerialName("value")@Contextual  val value : D, 
) : TContent
{
    override fun repr() : String = RT.structRepr("metadata", ArgumentEntry(false, null, value), )
}
