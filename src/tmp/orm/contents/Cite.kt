package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("cite")
@Serializable
data class TCite(
    @SerialName("key") val key : TLabel, 
    @SerialName("supplement") val supplement : TContentOrNone? = null, 
    @SerialName("form") val form : TNoneOrStr? = null, 
    @SerialName("style") val style : TAutoOrStr? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("cite", ArgumentEntry(false, null, key), ArgumentEntry(false, "supplement", supplement), ArgumentEntry(false, "form", form), ArgumentEntry(false, "style", style), )
}
