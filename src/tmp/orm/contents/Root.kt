package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("root")
@Serializable
data class TRoot(
    @SerialName("index") val index : TContentOrNone? = null, 
    @SerialName("radicand") val radicand : TContent, 
) : TContent
{
    override fun repr() : String = RT.reprOf(this)
}
