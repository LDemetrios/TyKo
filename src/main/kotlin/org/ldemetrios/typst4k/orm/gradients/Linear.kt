package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("linear")
@Serializable
data class TLinear(
    @SerialName("stops") val stops : TArray<TArrayOrColor<TColorOrRatio, >, >, 
    @SerialName("relative") val relative : TAutoOrStr? = null, 
    @SerialName("dir") val dir : TDirection? = null, 
    @SerialName("angle") val angle : TAngle? = null, 
) : TGradient
{
    override fun repr() : String = RT.structRepr("gradient.linear", Triple(true, null, stops), Triple(false, "relative", relative), Triple(false, "dir", dir), Triple(false, "angle", angle), )
}
