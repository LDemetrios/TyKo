package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("float")
@Serializable
data class TFloat(val value : Double) : TValue, 
    TFloatOrRatio, 
    TAutoOrFloat {
    override fun repr() : String = RT.reprOf(value)
    override fun toString() : String = value.toString()
}
