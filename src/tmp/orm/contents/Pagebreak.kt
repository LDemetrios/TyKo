package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("pagebreak")
@Serializable
data class TPagebreak(
    @SerialName("weak") val weak : TBool? = null, 
    @SerialName("to") val to : TNoneOrStr? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("pagebreak", ArgumentEntry(false, "weak", weak), ArgumentEntry(false, "to", to), )
}
