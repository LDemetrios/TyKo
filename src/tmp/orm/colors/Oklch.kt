package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("oklch")
@Serializable
data class TOklch(
    @SerialName("lightness") val lightness : TRatio, 
    @SerialName("chroma") val chroma : TFloatOrRatio, 
    @SerialName("hue") val hue : TAngle, 
    @SerialName("alpha") val alpha : TRatio? = null, 
) : TColor
{
    override fun repr() : String = RT.structRepr("oklch", ArgumentEntry(false, null, lightness), ArgumentEntry(false, null, chroma), ArgumentEntry(false, null, hue), ArgumentEntry(false, null, alpha), )
}
