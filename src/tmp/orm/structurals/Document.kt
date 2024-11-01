package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("document")
@Serializable
data class TDocument(
    @SerialName("title") val title : TContentOrNone? = null, 
    @SerialName("author") val author : TArrayOrStr<TStr, >? = null, 
    @SerialName("keywords") val keywords : TArrayOrStr<TStr, >? = null, 
    @SerialName("date") val date : TAutoOrDatetimeOrNone? = null, 
) : TStructural
{
    override fun repr() : String = RT.structRepr("document", ArgumentEntry(false, "title", title), ArgumentEntry(false, "author", author), ArgumentEntry(false, "keywords", keywords), ArgumentEntry(false, "date", date), )
}
