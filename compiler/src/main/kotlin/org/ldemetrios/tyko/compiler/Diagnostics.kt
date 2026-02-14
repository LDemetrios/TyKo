package org.ldemetrios.tyko.compiler

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Value attached to a specific [Span]
 */
@Serializable
data class Spanned<T>(
    @SerialName("v")
    val value: T,
    val span: Span
) {
    fun <R> map(func: (T) -> R) = Spanned(func(value), span)
}

/**
 * Type of native equivalent of stack trace element
 */
@Serializable
@JsonClassDiscriminator("type")
sealed class Tracepoint {
    /**
     * Error happened during call of the function. If it was defined immediately with the name, then this name is stored.
     */
    @Serializable
    @SerialName("Call")
    data class Call(val function: String?) : Tracepoint() {
        override val name get() = function
    }

    /**
     * Error happened during the application of the `show`-rule.
     */
    @Serializable
    @SerialName("Show")
    data class Show(val string: String) : Tracepoint() {
        override val name get() = string
    }

    /**
     * Error happened during the import.
     */
    @Serializable
    @SerialName("Import")
    data object Import : Tracepoint() {
        override val name get() = null
    }

    abstract val name: String?
}

/**
 * One diagnostic message originating from compilation, either Warning or Error.
 * Native equivalent of Throwable, except there can be more than one from one compilation.
 */
@Serializable
data class SourceDiagnostic(
    /**
     * Whether this is Warning or Error.
     */
    val severity: Severity,
    /**
     * [Span] of the error.
     */
    val span: Span,
    /**
     * Human-readable error message.
     */
    val message: String,
    /**
     * List of [Spanned] [Tracepoint]s, tracing the error. Native equivalent of stack trace.
     */
    val trace: List<Spanned<Tracepoint>>,
    /**
     * Set human-readable of hints, associated with the message.
     */
    val hints: List<String>
)

/**
 * The severity of a [SourceDiagnostic].
 */
@Serializable
enum class Severity {
    Error,
    Warning
}

/**
 * Value, possibly accompanied by warnings (list of [SourceDiagnostic]s with severity = Warning).
 */
@Serializable
data class Warned<T>(val output: T, val warnings: List<SourceDiagnostic>) {
    /**
     * Transform the underlying value, not affecting warnings.
     */
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