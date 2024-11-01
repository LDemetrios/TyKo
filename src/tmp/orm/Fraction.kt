package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("fraction")
@Serializable
data class TFraction(
    @SerialName("value") val value : TFloat, 
) : TValue, 
    TFractionOrRelative, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TAutoOrFractionOrRelative, 
    TContentOrFractionOrRelative, 
    TFractionOrNoneOrRelative
{
    override fun repr() : String = RT.reprOf(this)
}
