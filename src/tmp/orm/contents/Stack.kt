package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("stack")
@Serializable
data class TStack(
    @SerialName("children") val children : TArray<TContentOrFractionOrRelative, >, 
    @SerialName("dir") val dir : TDirection? = null, 
    @SerialName("spacing") val spacing : TFractionOrNoneOrRelative? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("stack", ArgumentEntry(true, null, children), ArgumentEntry(false, "dir", dir), ArgumentEntry(false, "spacing", spacing), )
}
