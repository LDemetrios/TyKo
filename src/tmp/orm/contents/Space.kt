package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("space")
@Serializable
data object TSpace : TContent
{
    override fun repr() : String = RT.reprOf(this)
}
