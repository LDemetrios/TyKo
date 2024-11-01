package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("bool")
@Serializable
data class TBool(val value : Boolean) : TValue, 
    TAutoOrBool, 
    TAutoOrBoolOrNoneOrRelative {
    override fun repr() : String = RT.reprOf(value)
    override fun toString() : String = value.toString()
}
