package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("datetime")
@Serializable
data class TDatetime(
    @SerialName("year") val year : TInt? = null, 
    @SerialName("month") val month : TInt? = null, 
    @SerialName("day") val day : TInt? = null, 
    @SerialName("hour") val hour : TInt? = null, 
    @SerialName("minute") val minute : TInt? = null, 
    @SerialName("second") val second : TInt? = null, 
) : TValue, 
    TAutoOrDatetimeOrNone
{
    override fun repr() : String = RT.structRepr("datetime", ArgumentEntry(false, "year", year), ArgumentEntry(false, "month", month), ArgumentEntry(false, "day", day), ArgumentEntry(false, "hour", hour), ArgumentEntry(false, "minute", minute), ArgumentEntry(false, "second", second), )
}
