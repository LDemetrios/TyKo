package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("binom")
@Serializable
data class TBinom(
    @SerialName("upper") val upper : TContent, 
    @SerialName("lower") val lower : TArray<TContent, >, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.binom", ArgumentEntry(false, null, upper), ArgumentEntry(true, null, lower), )
}
