package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("element-selector")
@Serializable
data class TElementSelector(
    @SerialName("element") val element : TStr, 
    @SerialName("where") val where : TDictionary<*, >? = null, 
) : TSelector
{
    override fun repr() : String = RT.reprOf(this)
}
