package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("grid")
@Serializable
data class TGrid(
    @SerialName("children") val children : TArray<TContent, >, 
    @SerialName("columns") val columns : TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null, 
    @SerialName("rows") val rows : TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null, 
    @SerialName("gutter") val gutter : TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null, 
    @SerialName("column-gutter") val columnGutter : TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null, 
    @SerialName("row-gutter") val rowGutter : TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null, 
    @SerialName("fill") val fill : TArrayOrColorOrGradientOrNoneOrPattern<*, >? = null, 
    @SerialName("align") val align : TAlignmentOrArrayOrAuto<*, >? = null, 
    @SerialName("stroke") val stroke : TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, *, >? = null, 
    @SerialName("inset") val inset : TArrayOrDictionaryOrRelative<*, *, >? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("grid", ArgumentEntry(true, null, children), ArgumentEntry(false, "columns", columns), ArgumentEntry(false, "rows", rows), ArgumentEntry(false, "gutter", gutter), ArgumentEntry(false, "column-gutter", columnGutter), ArgumentEntry(false, "row-gutter", rowGutter), ArgumentEntry(false, "fill", fill), ArgumentEntry(false, "align", align), ArgumentEntry(false, "stroke", stroke), ArgumentEntry(false, "inset", inset), )
}
