package org.ldemetrios.tyko.compiler

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.ldemetrios.tyko.driver.api.TyKoInternalApi

@OptIn(TyKoInternalApi::class)
@Serializable(with = RResultSerializer::class)
sealed class RResult<out T, out E> {
    @Serializable
    data class Ok<out T>(val value: T) : RResult<T, Nothing>()

    @Serializable
    data class Err<out E>(val error: E) : RResult<Nothing, E>()

    inline fun <R> map(transform: (T) -> R): RResult<R, E> = when (this) {
        is Ok -> Ok(transform(value))
        is Err -> this
    }

    inline fun <F> mapError(transform: (E) -> F): RResult<T, F> = when (this) {
        is Ok -> this
        is Err -> Err(transform(error))
    }

    inline fun <R> flatMap(transform: (T) -> RResult<R, @UnsafeVariance E>): RResult<R, E> = when (this) {
        is Ok -> transform(value)
        is Err -> this
    }
}

@TyKoInternalApi
class RResultSerializer<T, E>(
    private val tSerializer: KSerializer<T>,
    private val eSerializer: KSerializer<E>
) : KSerializer<RResult<T, E>> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("RResult") {
            element("Ok", tSerializer.descriptor, isOptional = true)
            element("Err", eSerializer.descriptor, isOptional = true)
        }

    override fun serialize(encoder: Encoder, value: RResult<T, E>) {
        val jsonEncoder = encoder as? kotlinx.serialization.json.JsonEncoder
            ?: error("Only JsonEncoder is supported")
        val jsonElement = when (value) {
            is RResult.Ok -> kotlinx.serialization.json.JsonObject(
                mapOf("Ok" to jsonEncoder.json.encodeToJsonElement(tSerializer, value.value))
            )

            is RResult.Err -> kotlinx.serialization.json.JsonObject(
                mapOf("Err" to jsonEncoder.json.encodeToJsonElement(eSerializer, value.error))
            )
        }
        jsonEncoder.encodeJsonElement(jsonElement)
    }

    override fun deserialize(decoder: Decoder): RResult<T, E> {
        val jsonDecoder = decoder as? kotlinx.serialization.json.JsonDecoder
            ?: error("Only JsonDecoder is supported")
        val jsonElement = jsonDecoder.decodeJsonElement()

        require(jsonElement is kotlinx.serialization.json.JsonObject) { "Expected JSON object" }

        return when {
            "Ok" in jsonElement -> RResult.Ok(
                jsonDecoder.json.decodeFromJsonElement(tSerializer, jsonElement["Ok"]!!)
            )

            "Err" in jsonElement -> RResult.Err(
                jsonDecoder.json.decodeFromJsonElement(eSerializer, jsonElement["Err"]!!)
            )

            else -> error("Invalid RResult format")
        }
    }
}
