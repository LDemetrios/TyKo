package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("regex-selector")
@Serializable
data class TRegexSelector(
    @SerialName("regex") val regex : TRegex, 
) : TSelector
{
    override fun repr() : String = RT.reprOf(this)
}
