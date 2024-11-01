package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("figure")
@Serializable
data class TFigure(
    @SerialName("body") val body : TContent, 
    @SerialName("placement") val placement : TAlignmentOrAutoOrNone? = null, 
    @SerialName("scope") val scope : TStr? = null, 
    @SerialName("caption") val caption : TContentOrNone? = null, 
    @SerialName("kind") val kind : TAutoOrStr? = null, 
    @SerialName("supplement") val supplement : TAutoOrContentOrNone? = null, 
    @SerialName("numbering") val numbering : TNoneOrStr? = null, 
    @SerialName("gap") val gap : TLength? = null, 
    @SerialName("outlined") val outlined : TBool? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("figure", ArgumentEntry(false, null, body), ArgumentEntry(false, "placement", placement), ArgumentEntry(false, "scope", scope), ArgumentEntry(false, "caption", caption), ArgumentEntry(false, "kind", kind), ArgumentEntry(false, "supplement", supplement), ArgumentEntry(false, "numbering", numbering), ArgumentEntry(false, "gap", gap), ArgumentEntry(false, "outlined", outlined), )
}
