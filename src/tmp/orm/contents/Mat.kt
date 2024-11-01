package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("mat")
@Serializable
data class TMat(
    @SerialName("rows") val rows : TArray<TArray<TContent, >, >, 
    @SerialName("delim") val delim : TArrayOrNoneOrStr<TNoneOrStr, >? = null, 
    @SerialName("augment") val augment : TDictionaryOrIntOrNone<*, >? = null, 
    @SerialName("align") val align : TAlignment? = null, 
    @SerialName("gap") val gap : TRelative? = null, 
    @SerialName("row-gap") val rowGap : TRelative? = null, 
    @SerialName("column-gap") val columnGap : TRelative? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.mat", ArgumentEntry(true, null, rows), ArgumentEntry(false, "delim", delim), ArgumentEntry(false, "augment", augment), ArgumentEntry(false, "align", align), ArgumentEntry(false, "gap", gap), ArgumentEntry(false, "row-gap", rowGap), ArgumentEntry(false, "column-gap", columnGap), )
}
