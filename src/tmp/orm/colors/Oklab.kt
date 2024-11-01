package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("oklab")
@Serializable
data class TOklab(
    @SerialName("lightness") val lightness : TRatio, 
    @SerialName("a") val a : TFloatOrRatio, 
    @SerialName("b") val b : TFloatOrRatio, 
    @SerialName("alpha") val alpha : TRatio? = null, 
) : TColor
{
    override fun repr() : String = RT.structRepr("oklab", ArgumentEntry(false, null, lightness), ArgumentEntry(false, null, a), ArgumentEntry(false, null, b), ArgumentEntry(false, null, alpha), )
}
