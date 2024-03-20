package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("math.frac")
@Serializable
data class TFrac(
    @SerialName("num") val num : TContent, 
    @SerialName("denom") val denom : TContent, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.frac", null to num, null to denom, )
}
