package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("int")
@Serializable
data class TInt(val value : Long) : TValue, 
    TIntOrRatio, 
    TAutoOrInt, 
    TIntOrLength, 
    TIntOrNone, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TIntOrStr, 
    TArrayOrIntOrNone<Nothing>, 
    TDictionaryOrIntOrNone<Nothing> {
    override fun repr() : String = RT.reprOf(value)
    override fun toString() : String = value.toString()
}
