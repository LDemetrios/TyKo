package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("after-selector")
@Serializable
data class TAfterSelector(
    @SerialName("selector") val selector : TSelector, 
    @SerialName("start") val start : TSelector, 
    @SerialName("inclusive") val inclusive : TBool? = null, 
) : TSelector
{
    override fun repr() : String = RT.reprOf(this)
}
