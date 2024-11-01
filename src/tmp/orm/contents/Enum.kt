package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("enum")
@Serializable
data class TEnum(
    @SerialName("tight") val tight : TBool? = null, 
    @SerialName("numbering") val numbering : TStr? = null, 
    @SerialName("start") val start : TInt? = null, 
    @SerialName("full") val full : TBool? = null, 
    @SerialName("indent") val indent : TLength? = null, 
    @SerialName("body-indent") val bodyIndent : TLength? = null, 
    @SerialName("spacing") val spacing : TAutoOrFractionOrRelative? = null, 
    @SerialName("number-align") val numberAlign : TAlignment? = null, 
    @SerialName("children") val children : TArray<TArrayOrContent<*, >, >, 
) : TContent
{
    override fun repr() : String = RT.structRepr("enum", ArgumentEntry(false, "tight", tight), ArgumentEntry(false, "numbering", numbering), ArgumentEntry(false, "start", start), ArgumentEntry(false, "full", full), ArgumentEntry(false, "indent", indent), ArgumentEntry(false, "body-indent", bodyIndent), ArgumentEntry(false, "spacing", spacing), ArgumentEntry(false, "number-align", numberAlign), ArgumentEntry(true, null, children), )
}
