package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("module")
@Serializable
data class TModule(
    @SerialName("name") val name : TStr, 
) : TValue
{
    override fun repr() : String = RT.reprOf(this)
}
