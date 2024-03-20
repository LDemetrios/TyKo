package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("figure")
@Serializable
data class TFigure(
    @SerialName("body") val body : TContent, 
    @SerialName("placement") val placement : TAlignmentOrAutoOrNone? = null, 
    @SerialName("caption") val caption : TContentOrNone? = null, 
    @SerialName("kind") val kind : TAutoOrStr? = null, 
    @SerialName("supplement") val supplement : TAutoOrContentOrNone? = null, 
    @SerialName("numbering") val numbering : TNoneOrStr? = null, 
    @SerialName("gap") val gap : TLength? = null, 
    @SerialName("outlined") val outlined : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("figure", null to body, "placement" to placement, "caption" to caption, "kind" to kind, "supplement" to supplement, "numbering" to numbering, "gap" to gap, "outlined" to outlined, )
}
