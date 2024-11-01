package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("v")
@Serializable
data class TV(
    @SerialName("amount") val amount : TFractionOrRelative, 
    @SerialName("weak") val weak : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("v", ArgumentEntry(false, null, amount), ArgumentEntry(false, "weak", weak), )
}
