package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("and-selector")
@Serializable
data class TAndSelector(
    @SerialName("variants") val variants : TArray<TSelector, >, 
) : TSelector
{
    override fun repr() : String = RT.reprOf(this)
}
