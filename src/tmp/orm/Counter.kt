package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("counter")
@Serializable
data class TCounter(
    @SerialName("value") val value : TCounterKey, 
) : TValue
{
    override fun repr() : String = RT.reprOf(this)
}
