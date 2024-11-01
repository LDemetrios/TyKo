package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("color.hsl")
@Serializable
data class THsl(
    @SerialName("hue") val hue : TAngle, 
    @SerialName("saturation") val saturation : TIntOrRatio, 
    @SerialName("lightness") val lightness : TIntOrRatio, 
    @SerialName("alpha") val alpha : TIntOrRatio? = null, 
) : TColor
{
    override fun repr() : String = RT.structRepr("color.hsl", ArgumentEntry(false, null, hue), ArgumentEntry(false, null, saturation), ArgumentEntry(false, null, lightness), ArgumentEntry(false, null, alpha), )
}
