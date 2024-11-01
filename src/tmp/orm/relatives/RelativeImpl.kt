package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("relative-impl")
@Serializable
data class TRelativeImpl(
    @SerialName("rel") val rel : TRatio? = null, 
    @SerialName("abs") val abs : TLength? = null, 
) : TRelative
{
    override fun repr() : String = RT.reprOf(this)
}
