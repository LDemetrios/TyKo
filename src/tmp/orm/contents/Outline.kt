package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("outline")
@Serializable
data class TOutline(
    @SerialName("title") val title : TAutoOrContentOrNone? = null, 
    @SerialName("target") val target : TLabelOrLocationOrSelector? = null, 
    @SerialName("depth") val depth : TIntOrNone? = null, 
    @SerialName("indent") val indent : TAutoOrBoolOrNoneOrRelative? = null, 
    @SerialName("fill") val fill : TContentOrNone? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("outline", ArgumentEntry(false, "title", title), ArgumentEntry(false, "target", target), ArgumentEntry(false, "depth", depth), ArgumentEntry(false, "indent", indent), ArgumentEntry(false, "fill", fill), )
}
