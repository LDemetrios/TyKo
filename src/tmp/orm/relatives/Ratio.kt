package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("ratio")
@Serializable
data class TRatio(
    @SerialName("value") val value : TFloat, 
) : TRelative, 
    TIntOrRatio, 
    TFloatOrRatio, 
    TColorOrRatio, 
    TAutoOrLengthOrRatio
{
    override fun repr() : String = RT.reprOf(this)
}
