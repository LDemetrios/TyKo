package org.ldemetrios.tyko.compiler

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
data class Spanned<T>(
    @SerialName("v")
    val value: T,
    val span: Span
)

@Serializable
@JsonClassDiscriminator("type")
sealed class Tracepoint {
    @Serializable
    @SerialName("Call")
    data class Call(val function: String?) : Tracepoint() {
        override val name get() = function
    }

    @Serializable
    @SerialName("Show")
    data class Show(val string: String) : Tracepoint() {
        override val name get() = string
    }

    @Serializable
    @SerialName("Import")
    data object Import : Tracepoint() {
        override val name get() = null
    }

    abstract val name: String?
}

@Serializable
data class SourceDiagnostic(
    val severity: Severity,
    val span: Span,
    val message: String,
    val trace: List<Spanned<Tracepoint>>,
    val hints: List<String>
)

@Serializable
enum class Severity {
    Error,
    Warning
}

@Serializable
data class Warned<T>(val output: T, val warnings: List<SourceDiagnostic>) {
    fun <R> map(f: (T) -> R): Warned<R> = Warned(f(output), warnings)
}


@JvmName("generic_sanitize")
internal fun <T> Warned<T>.sanitize() = Warned(
    output,
    warnings.sanitize(),
)

internal fun <T> Warned<RResult<T, List<SourceDiagnostic>>>.sanitize() = Warned(
    output.sanitize(),
    warnings.sanitize(),
)

internal fun <T> RResult<T, List<SourceDiagnostic>>.sanitize() = mapError { it.sanitize() }
internal fun List<SourceDiagnostic>.sanitize() = map { it.sanitize() }
internal fun SourceDiagnostic.sanitize() =
    copy(hints = hints.filter { it != "set `RUST_BACKTRACE` to `1` or `full` to capture a backtrace" })