package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("equation")
@Serializable
data class TEquation(
    @SerialName("body") val body : TContent, 
    @SerialName("block") val block : TBool? = null, 
    @SerialName("numbering") val numbering : TNoneOrStr? = null, 
    @SerialName("number-align") val numberAlign : TAlignment? = null, 
    @SerialName("supplement") val supplement : TAutoOrContentOrNone? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("math.equation", ArgumentEntry(false, null, body), ArgumentEntry(false, "block", block), ArgumentEntry(false, "numbering", numbering), ArgumentEntry(false, "number-align", numberAlign), ArgumentEntry(false, "supplement", supplement), )
}
