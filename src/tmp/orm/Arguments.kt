package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("arguments")
@Serializable
data class TArguments<out A : TValue>(
    @SerialName("positional") val positional : TArray<A, >, 
    @SerialName("named") val named : TDictionary<A, >, 
) : TValue
{
    override fun repr() : String = RT.reprOf(this)
}
