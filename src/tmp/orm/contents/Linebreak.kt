package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("linebreak")
@Serializable
data class TLinebreak(
    @SerialName("justify") val justify : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("linebreak", ArgumentEntry(false, "justify", justify), )
}
