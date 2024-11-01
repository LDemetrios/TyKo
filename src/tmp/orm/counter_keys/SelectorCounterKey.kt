package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("selector-counter-key")
@Serializable
data class TSelectorCounterKey(
    @SerialName("selector") val selector : TSelector, 
) : TCounterKey
{
    override fun repr() : String = RT.structRepr("selector-counter-key", ArgumentEntry(false, "selector", selector), )
}
