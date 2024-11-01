package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("raw")
@Serializable
data class TRaw(
    @SerialName("text") val text : TStr, 
    @SerialName("block") val block : TBool? = null, 
    @SerialName("lang") val lang : TNoneOrStr? = null, 
    @SerialName("align") val align : TAlignment? = null, 
    @SerialName("syntaxes") val syntaxes : TArrayOrStr<*, >? = null, 
    @SerialName("theme") val theme : TAutoOrNoneOrStr? = null, 
    @SerialName("tab-size") val tabSize : TInt? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("raw", ArgumentEntry(false, null, text), ArgumentEntry(false, "block", block), ArgumentEntry(false, "lang", lang), ArgumentEntry(false, "align", align), ArgumentEntry(false, "syntaxes", syntaxes), ArgumentEntry(false, "theme", theme), ArgumentEntry(false, "tab-size", tabSize), )
}
