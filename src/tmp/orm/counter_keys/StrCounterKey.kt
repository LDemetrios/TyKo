package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("str-counter-key")
@Serializable
data class TStrCounterKey(
    @SerialName("str") val str : TStr, 
) : TCounterKey
{
    override fun repr() : String = RT.structRepr("str-counter-key", ArgumentEntry(false, "str", str), )
}
