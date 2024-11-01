package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("page")
@Serializable
data class TPage(
    @SerialName("body") val body : TContent, 
    @SerialName("paper") val paper : TStr? = null, 
    @SerialName("width") val width : TAutoOrLength? = null, 
    @SerialName("height") val height : TAutoOrLength? = null, 
    @SerialName("flipped") val flipped : TBool? = null, 
    @SerialName("margin") val margin : TAutoOrDictionaryOrRelative<*, >? = null, 
    @SerialName("binding") val binding : TAlignmentOrAuto? = null, 
    @SerialName("columns") val columns : TInt? = null, 
    @SerialName("fill") val fill : TColorOrGradientOrNoneOrPattern? = null, 
    @SerialName("numbering") val numbering : TNoneOrStr? = null, 
    @SerialName("number-align") val numberAlign : TAlignment? = null, 
    @SerialName("header") val header : TAutoOrContentOrNone? = null, 
    @SerialName("header-ascent") val headerAscent : TRelative? = null, 
    @SerialName("footer") val footer : TAutoOrContentOrNone? = null, 
    @SerialName("footer-descent") val footerDescent : TRelative? = null, 
    @SerialName("background") val background : TContentOrNone? = null, 
    @SerialName("foreground") val foreground : TContentOrNone? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("page", ArgumentEntry(false, null, body), ArgumentEntry(false, "paper", paper), ArgumentEntry(false, "width", width), ArgumentEntry(false, "height", height), ArgumentEntry(false, "flipped", flipped), ArgumentEntry(false, "margin", margin), ArgumentEntry(false, "binding", binding), ArgumentEntry(false, "columns", columns), ArgumentEntry(false, "fill", fill), ArgumentEntry(false, "numbering", numbering), ArgumentEntry(false, "number-align", numberAlign), ArgumentEntry(false, "header", header), ArgumentEntry(false, "header-ascent", headerAscent), ArgumentEntry(false, "footer", footer), ArgumentEntry(false, "footer-descent", footerDescent), ArgumentEntry(false, "background", background), ArgumentEntry(false, "foreground", foreground), )
}
