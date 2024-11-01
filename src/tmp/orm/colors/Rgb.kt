package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("rgb")
@Serializable
data class TRgb(
    @SerialName("hex") val hex : TStr, 
) : TColor
{
    override fun repr() : String = RT.structRepr("rgb", ArgumentEntry(false, null, hex), )
}
