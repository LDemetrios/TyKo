package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("label-selector")
@Serializable
data class TLabelSelector(
    @SerialName("label") val label : TLabel, 
) : TSelector
{
    override fun repr() : String = RT.reprOf(this)
}
