package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("plugin")
@Serializable
data object TPlugin : TValue
{
    override fun repr() : String = RT.structRepr("plugin", )
}
