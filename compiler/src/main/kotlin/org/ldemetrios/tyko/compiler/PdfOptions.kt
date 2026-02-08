package org.ldemetrios.tyko.compiler

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("type")
sealed class PdfTimezone {
    @Serializable
    @SerialName("Utc")
    data object Utc : PdfTimezone()

    @Serializable
    @SerialName("Local")
    data class Local(val hourOffset: Int, val minuteOffset: Int) : PdfTimezone()
}

@Serializable
data class PdfTimestamp(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val timezone: PdfTimezone? = null,
)

@Serializable
data class PdfPageRange(
    val start: Int? = null,
    val end: Int? = null,
)

@Serializable
enum class PdfValidator {
    None,
    A1_A,
    A1_B,
    A2_A,
    A2_B,
    A2_U,
    A3_A,
    A3_B,
    A3_U,
    UA1,
    A4,
    A4F,
    A4E,
}

@Serializable
enum class PdfVersion {
    Pdf14,
    Pdf15,
    Pdf16,
    Pdf17,
    Pdf20,
}

@Serializable
data class PdfStandards(
    val validator: PdfValidator? = null,
    val version: PdfVersion? = null,
)

@Serializable
data class PdfOptionsConfig(
    val timestamp: PdfTimestamp? = null,
    val pageRanges: List<PdfPageRange>? = null,
    val standards: PdfStandards? = null,
    val tagged: Boolean? = null,
)
