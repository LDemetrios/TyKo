package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("angle")
@Serializable
data class TAngle(
    @SerialName("deg") val deg : TFloat, 
) : TValue, 
    TAngleOrAuto
{
    override fun repr() : String = RT.reprOf(this)
}
