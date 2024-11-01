package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("vec")
@Serializable
data class TVec(
    @SerialName("children") val children : TArray<TContent, >, 
    @SerialName("delim") val delim : TArrayOrNoneOrStr<TNoneOrStr, >? = null, 
    @SerialName("align") val align : TAlignment? = null, 
    @SerialName("gap") val gap : TRelative? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.vec", ArgumentEntry(true, null, children), ArgumentEntry(false, "delim", delim), ArgumentEntry(false, "align", align), ArgumentEntry(false, "gap", gap), )
}
