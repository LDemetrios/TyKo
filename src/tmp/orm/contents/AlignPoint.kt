package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("align-point")
@Serializable
data object TAlignPoint : TContent
{
    override fun repr() : String = RT.reprOf(this)
}
