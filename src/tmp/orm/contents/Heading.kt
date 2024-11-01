package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("heading")
@Serializable
data class THeading(
    @SerialName("body") val body : TContent, 
    @SerialName("level") val level : TAutoOrInt? = null, 
    @SerialName("depth") val depth : TInt? = null, 
    @SerialName("offset") val offset : TInt? = null, 
    @SerialName("numbering") val numbering : TNoneOrStr? = null, 
    @SerialName("supplement") val supplement : TAutoOrContentOrNone? = null, 
    @SerialName("outlined") val outlined : TBool? = null, 
    @SerialName("bookmarked") val bookmarked : TAutoOrBool? = null, 
    @SerialName("hanging-indent") val hangingIndent : TAutoOrLength? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("heading", ArgumentEntry(false, null, body), ArgumentEntry(false, "level", level), ArgumentEntry(false, "depth", depth), ArgumentEntry(false, "offset", offset), ArgumentEntry(false, "numbering", numbering), ArgumentEntry(false, "supplement", supplement), ArgumentEntry(false, "outlined", outlined), ArgumentEntry(false, "bookmarked", bookmarked), ArgumentEntry(false, "hanging-indent", hangingIndent), )
}
