package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("regex")
@Serializable
data class TRegex(
    @SerialName("regex") val regex : TStr? = null, 
) : TValue
{
    override fun repr() : String = RT.structRepr("regex", ArgumentEntry(false, null, regex), )
}
