package org.ldemetrios.tyko.compiler

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ldemetrios.tyko.driver.api.RawNow

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class Now {
    @Serializable
    @SerialName("System")
    data object System : Now()

    @Serializable
    @SerialName("Fixed")
    data class Fixed(val millis: Long, val nanos: Int) : Now()
}

internal fun Now?.toRawNow() = when (this) {
    null -> RawNow(-1, 0)
    is Now.System -> RawNow(-2, 0)
    is Now.Fixed -> RawNow(millis, nanos)
}

