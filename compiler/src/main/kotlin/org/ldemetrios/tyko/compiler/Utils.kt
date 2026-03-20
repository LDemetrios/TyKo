package org.ldemetrios.tyko.compiler

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal inline fun <reified T> T.serialize(): String = Json.encodeToString<T>(this)

internal inline fun <reified T> String.deserialize(): T = Json.decodeFromString<T>(this)

fun <T> RResult<T, List<SourceDiagnostic>>.orThrow(): T = when (this) {
    is RResult.Ok -> value
    is RResult.Err -> throw TypstCompilerException.construct(error)
}

fun <T> Warned<RResult<T, List<SourceDiagnostic>>>.outputOrThrow(): T = output.orThrow()

fun <T> Warned<RResult<T, List<SourceDiagnostic>>>.warnedOrThrow(): Warned<T> = map { it.orThrow() }
