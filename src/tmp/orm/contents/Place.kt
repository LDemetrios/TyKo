package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("place")
@Serializable
data class TPlace(
    @SerialName("alignment") val alignment : TAlignmentOrAuto? = null, 
    @SerialName("body") val body : TContent, 
    @SerialName("scope") val scope : TStr? = null, 
    @SerialName("float") val float : TBool? = null, 
    @SerialName("clearance") val clearance : TLength? = null, 
    @SerialName("dx") val dx : TRelative? = null, 
    @SerialName("dy") val dy : TRelative? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("place", ArgumentEntry(false, null, alignment), ArgumentEntry(false, null, body), ArgumentEntry(false, "scope", scope), ArgumentEntry(false, "float", float), ArgumentEntry(false, "clearance", clearance), ArgumentEntry(false, "dx", dx), ArgumentEntry(false, "dy", dy), )
}
