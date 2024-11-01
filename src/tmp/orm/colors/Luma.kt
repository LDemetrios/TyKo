package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("luma")
@Serializable
data class TLuma(
    @SerialName("lightness") val lightness : TIntOrRatio, 
    @SerialName("alpha") val alpha : TRatio? = null, 
) : TColor
{
    override fun repr() : String = RT.structRepr("luma", ArgumentEntry(false, null, lightness), ArgumentEntry(false, null, alpha), )
}
