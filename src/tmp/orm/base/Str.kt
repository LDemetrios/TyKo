package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("str")
@Serializable
data class TStr(val value : String) : TValue, 
    TAutoOrStr, 
    TArrayOrStr<Nothing>, 
    TNoneOrStr, 
    TLengthOrStr, 
    TDictionaryOrLabelOrLocationOrStr<Nothing>, 
    TAutoOrNoneOrStr, 
    TArrayOrAutoOrDictionaryOrStr<Nothing, Nothing>, 
    TIntOrStr, 
    TContentOrStr, 
    TArrayOrNoneOrStr<Nothing>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing> {
    override fun repr() : String = RT.reprOf(value)
    override fun toString() : String = value.toString()
}
