package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("list")
@Serializable
data class TList(
    @SerialName("tight") val tight : TBool? = null, 
    @SerialName("marker") val marker : TArrayOrContent<TContent, >? = null, 
    @SerialName("indent") val indent : TLength? = null, 
    @SerialName("body-indent") val bodyIndent : TLength? = null, 
    @SerialName("spacing") val spacing : TAutoOrFractionOrRelative? = null, 
    @SerialName("children") val children : TArray<TListItem, >, 
) : TContent
{
    override fun repr() : String = RT.structRepr("list", ArgumentEntry(false, "tight", tight), ArgumentEntry(false, "marker", marker), ArgumentEntry(false, "indent", indent), ArgumentEntry(false, "body-indent", bodyIndent), ArgumentEntry(false, "spacing", spacing), ArgumentEntry(true, null, children), )
}
