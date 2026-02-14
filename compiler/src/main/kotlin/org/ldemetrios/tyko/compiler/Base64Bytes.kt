package org.ldemetrios.tyko.compiler

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ldemetrios.tyko.driver.api.TyKoInternalApi
import java.util.Base64

/**
 * A wrapper for ByteArray, providing serialization as base-64 string
 */
@Serializable(with = Base64BytesSerializer::class)
@TyKoInternalApi
data class Base64Bytes(val bytes: ByteArray) {
    override fun equals(other: Any?): Boolean =
        this === other || other is Base64Bytes && bytes.contentEquals(other.bytes)

    override fun hashCode(): Int = bytes.contentHashCode()
}

@TyKoInternalApi
object Base64BytesSerializer : KSerializer<Base64Bytes> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Base64Bytes", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Base64Bytes) {
        val encoded = Base64.getEncoder().encodeToString(value.bytes)
        encoder.encodeString(encoded)
    }

    override fun deserialize(decoder: Decoder): Base64Bytes {
        val decoded = Base64.getDecoder().decode(decoder.decodeString())
        return Base64Bytes(decoded)
    }
}
