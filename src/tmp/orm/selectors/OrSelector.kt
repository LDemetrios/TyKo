package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("or-selector")
@Serializable
data class TOrSelector(
    @SerialName("variants") val variants : TArray<TSelector, >, 
) : TSelector
{
    override fun repr() : String = RT.reprOf(this)
}
