package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("label")
@Serializable
data class TLabel(
    @SerialName("name") val name : TStr, 
) : TValue, 
    TContentOrLabel, 
    TDictionaryOrLabelOrLocationOrStr<Nothing>, 
    TLabelOrLocationOrSelector, 
    TContentOrLabelOrNone
{
    override fun repr() : String = RT.structRepr("label", ArgumentEntry(false, null, name), )
}
