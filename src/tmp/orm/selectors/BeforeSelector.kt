package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("before-selector")
@Serializable
data class TBeforeSelector(
    @SerialName("selector") val selector : TSelector, 
    @SerialName("end") val end : TSelector, 
    @SerialName("inclusive") val inclusive : TBool? = null, 
) : TSelector
{
    override fun repr() : String = RT.reprOf(this)
}
