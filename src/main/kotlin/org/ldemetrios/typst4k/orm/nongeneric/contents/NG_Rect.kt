@file:Suppress("UNNECESSARY_SAFE_CALL", "KotlinRedundantDiagnosticSuppress")

package org.ldemetrios.typst4k.orm.nongeneric

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast
import org.ldemetrios.typst4k.orm.*


@SerialName("rect")
@Serializable
data class NGTRect(
    @SerialName("width") val width : NGTAutoOrRelative? = null, 
    @SerialName("height") val height : NGTAutoOrRelative? = null, 
    @SerialName("fill") val fill : NGTColorOrGradientOrNoneOrPattern? = null, 
    @SerialName("stroke") val stroke : NGTAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null, 
    @SerialName("radius") val radius : NGTDictionaryOrRelative<*, >? = null, 
    @SerialName("inset") val inset : NGTDictionaryOrRelative<*, >? = null, 
    @SerialName("outset") val outset : NGTDictionaryOrRelative<*, >? = null, 
    @SerialName("body") val body : NGTContentOrNone? = null, 
) : NGTContent
{
    override fun convert() = TRect(width?.convert().cast(), height?.convert().cast(), fill?.convert().cast(), stroke?.convert().cast(), radius?.convert().cast(), inset?.convert().cast(), outset?.convert().cast(), body?.convert().cast())
}
