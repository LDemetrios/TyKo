package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@Deprecated("styled is deprecated, exists only for deserialization compatibility")
@SerialName("styled")
@Serializable
data class TStyled(
    @SerialName("child") val child : TContent, 
) : TContent
{
    override fun repr() : String = RT.reprOf(this)
}
