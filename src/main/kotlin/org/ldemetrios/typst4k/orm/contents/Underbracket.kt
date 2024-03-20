package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("math.underbracket")
@Serializable
data class TUnderbracket(
    @SerialName("body") val body : TContent, 
    @SerialName("annotation") val annotation : TContentOrNone? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.underbracket", null to body, null to annotation, )
}
