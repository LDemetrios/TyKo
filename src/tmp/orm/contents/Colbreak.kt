package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("colbreak")
@Serializable
data class TColbreak(
    @SerialName("weak") val weak : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("colbreak", ArgumentEntry(false, "weak", weak), )
}
