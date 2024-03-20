@file:Suppress("UNNECESSARY_SAFE_CALL", "KotlinRedundantDiagnosticSuppress")

package org.ldemetrios.typst4k.orm.nongeneric

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast
import org.ldemetrios.typst4k.orm.*


@SerialName("move")
@Serializable
data class NGTMove(
    @SerialName("dx") val dx : NGTRelative? = null, 
    @SerialName("dy") val dy : NGTRelative? = null, 
    @SerialName("body") val body : NGTContent, 
) : NGTContent
{
    override fun convert() = TMove(dx?.convert().cast(), dy?.convert().cast(), body?.convert().cast())
}
