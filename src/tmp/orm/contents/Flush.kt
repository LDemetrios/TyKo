package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("place.flush")
@Serializable
data object TFlush : TContent
{
    override fun repr() : String = RT.structRepr("place.flush", )
}
