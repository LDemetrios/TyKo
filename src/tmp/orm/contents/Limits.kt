package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("limits")
@Serializable
data class TLimits(
    @SerialName("body") val body : TContent, 
    @SerialName("inline") val inline : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.limits", ArgumentEntry(false, null, body), ArgumentEntry(false, "inline", inline), )
}
