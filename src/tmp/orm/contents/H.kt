package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("h")
@Serializable
data class TH(
    @SerialName("amount") val amount : TFractionOrRelative, 
    @SerialName("weak") val weak : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("h", ArgumentEntry(false, null, amount), ArgumentEntry(false, "weak", weak), )
}
