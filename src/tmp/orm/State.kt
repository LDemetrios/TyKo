package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("state")
@Serializable
data class TState(
    @SerialName("key") val key : TStr, 
    @SerialName("init") val init : TValue? = null, 
) : TValue
{
    override fun repr() : String = RT.structRepr("state", ArgumentEntry(false, null, key), ArgumentEntry(false, null, init), )
}
