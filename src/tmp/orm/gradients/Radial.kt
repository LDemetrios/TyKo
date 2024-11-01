package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("radial")
@Serializable
data class TRadial(
    @SerialName("stops") val stops : TArray<TArrayOrColor<TColorOrRatio, >, >, 
    @SerialName("relative") val relative : TAutoOrStr? = null, 
    @SerialName("center") val center : TArray<TRatio, >? = null, 
    @SerialName("radius") val radius : TRatio? = null, 
    @SerialName("focal-center") val focalCenter : TArrayOrAuto<TRatio, >? = null, 
    @SerialName("focal-radius") val focalRadius : TRatio? = null, 
) : TGradient
{
    override fun repr() : String = RT.structRepr("gradient.radial", ArgumentEntry(true, null, stops), ArgumentEntry(false, "relative", relative), ArgumentEntry(false, "center", center), ArgumentEntry(false, "radius", radius), ArgumentEntry(false, "focal-center", focalCenter), ArgumentEntry(false, "focal-radius", focalRadius), )
}
