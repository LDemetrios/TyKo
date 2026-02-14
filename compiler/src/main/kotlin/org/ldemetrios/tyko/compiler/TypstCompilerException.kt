@file:JvmName("TypstCompilerExceptionKt")

package org.ldemetrios.tyko.compiler

/**
 * Exception, representing a failed attempt to compile or evaluate the document.
 * Should usually be created via [construct].
 */
class TypstCompilerException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)

    companion object {
        /**
         * Performs an attempt to transform Typst's own representation of errors (List of [SourceDiagnostic]s)
         * into JVM-like way, that is, [StackTraceElement]s. Multiple errors will be represented as [Throwable.getSuppressed].
         */
        fun construct(trace: List<SourceDiagnostic>, cause: Throwable? = null): TypstCompilerException {
            val exc = trace.map { TypstCompilerException(it, cause) }
            val res = exc.first()
            for (another in exc.drop(1)) res.addSuppressed(another)
            return res
        }
    }
}

private fun Span.toSTE(tracepoint: Tracepoint?) = StackTraceElement(
    file?.packageSpec?.namespace,
    file?.packageSpec?.name,
    file?.packageSpec?.version.toString(),
    file?.virtualPath?.toString()?.drop(1) ?: "<detached>",
    when (tracepoint) {
        null -> "<source>"
        is Tracepoint.Call -> tracepoint.name ?: "<Tracepoint.Call>"
        Tracepoint.Import -> "<import>"
        is Tracepoint.Show -> tracepoint.name?.let { "show $it" } ?: "<Tracepoint.Show>"
    },
    file?.virtualPath?.toString()?.split("/")?.last() ?: "<detached>",
    startLine.toInt() + 1,
)

private fun TypstCompilerException(diagnostic: SourceDiagnostic, cause: Throwable? = null): TypstCompilerException {
    val exc = TypstCompilerException(diagnostic.message, cause)
    val stack = exc.stackTrace.asList().asReversed().dropLastWhile { it.className == "org.ldemetrios.tyko.compiler.TypstCompilerExceptionKt" }.toMutableList()

    for (idx in diagnostic.trace.indices.reversed()) {
        stack.add(diagnostic.trace[idx].span.toSTE(diagnostic.trace.getOrNull(idx + 1)?.value))
    }
    stack.add(diagnostic.span.toSTE(diagnostic.trace.firstOrNull()?.value))

    exc.stackTrace = stack.asReversed().toTypedArray()
    return exc
}