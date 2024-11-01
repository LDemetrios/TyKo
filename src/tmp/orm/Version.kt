package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("version")
@Serializable
data class TVersion(
    @SerialName("value") val value : TArray<TInt, >, 
) : TValue
{
    override fun repr() : String = RT.structRepr("version", ArgumentEntry(false, null, value), )
}
