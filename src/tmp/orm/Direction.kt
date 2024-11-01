package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("direction")
@Serializable
data class TDirection(
    @SerialName("value") val value : TStr, 
) : TValue, 
    TAutoOrDirection
{
    override fun repr() : String = RT.reprOf(this)
}
