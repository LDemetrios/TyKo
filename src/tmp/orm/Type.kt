package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("type")
@Serializable
data class TType(
    @SerialName("name") val name : TStr, 
) : TValue
{
    override fun repr() : String = RT.reprOf(this)
}
