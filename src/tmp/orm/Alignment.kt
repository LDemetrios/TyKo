package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("alignment")
@Serializable
data class TAlignment(
    @SerialName("horizontal") val horizontal : TStr? = null, 
    @SerialName("vertical") val vertical : TStr? = null, 
) : TValue, 
    TAlignmentOrAutoOrNone, 
    TAlignmentOrAuto, 
    TAlignmentOrArrayOrAuto<Nothing>
{
    override fun repr() : String = RT.reprOf(this)
}
