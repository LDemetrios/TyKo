package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("image")
@Serializable
data class TImage(
    @SerialName("path") val path : TStr, 
    @SerialName("format") val format : TAutoOrStr? = null, 
    @SerialName("width") val width : TAutoOrRelative? = null, 
    @SerialName("height") val height : TAutoOrRelative? = null, 
    @SerialName("alt") val alt : TNoneOrStr? = null, 
    @SerialName("fit") val fit : TStr? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("image", ArgumentEntry(false, null, path), ArgumentEntry(false, "format", format), ArgumentEntry(false, "width", width), ArgumentEntry(false, "height", height), ArgumentEntry(false, "alt", alt), ArgumentEntry(false, "fit", fit), )
}
