package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("cmyk")
@Serializable
data class TCmyk(
    @SerialName("cyan") val cyan : TRatio, 
    @SerialName("magenta") val magenta : TRatio, 
    @SerialName("yellow") val yellow : TRatio, 
    @SerialName("key") val key : TRatio, 
) : TColor
{
    override fun repr() : String = RT.structRepr("cmyk", ArgumentEntry(false, null, cyan), ArgumentEntry(false, null, magenta), ArgumentEntry(false, null, yellow), ArgumentEntry(false, null, key), )
}
