package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("decimal")
@Serializable
data class TDecimal(
    @SerialName("value") val value : TStr? = null, 
) : TValue
{
    override fun repr() : String = RT.structRepr("decimal", ArgumentEntry(false, null, value), )
}
