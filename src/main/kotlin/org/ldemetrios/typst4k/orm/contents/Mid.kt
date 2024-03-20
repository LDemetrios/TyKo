package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("math.mid")
@Serializable
data class TMid(
    @SerialName("body") val body : TContent, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.mid", null to body, )
}
