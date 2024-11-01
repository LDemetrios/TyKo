package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("page-counter-key")
@Serializable
data object TPageCounterKey : TCounterKey
{
    override fun repr() : String = RT.structRepr("page-counter-key", )
}
