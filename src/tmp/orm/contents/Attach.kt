package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("attach")
@Serializable
data class TAttach(
    @SerialName("base") val base : TContent, 
    @SerialName("t") val t : TContentOrNone? = null, 
    @SerialName("b") val b : TContentOrNone? = null, 
    @SerialName("tl") val tl : TContentOrNone? = null, 
    @SerialName("bl") val bl : TContentOrNone? = null, 
    @SerialName("tr") val tr : TContentOrNone? = null, 
    @SerialName("br") val br : TContentOrNone? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.attach", ArgumentEntry(false, null, base), ArgumentEntry(false, "t", t), ArgumentEntry(false, "b", b), ArgumentEntry(false, "tl", tl), ArgumentEntry(false, "bl", bl), ArgumentEntry(false, "tr", tr), ArgumentEntry(false, "br", br), )
}
