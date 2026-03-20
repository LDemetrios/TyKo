package org.ldemetrios.tyko.compiler

import org.ldemetrios.tyko.driver.api.JavaObjectsRegistrar
import org.ldemetrios.tyko.driver.api.Pointer
import org.ldemetrios.tyko.driver.api.PointerAccess
import org.ldemetrios.tyko.driver.api.PointerDebugInfo
import org.ldemetrios.tyko.driver.api.PointerType
import org.ldemetrios.tyko.driver.api.Resource
import org.ldemetrios.tyko.driver.api.ResourceDebugInfo
import org.ldemetrios.tyko.driver.api.ResourceReference
import org.ldemetrios.tyko.driver.api.TyKoInternalApi
import org.ldemetrios.tyko.driver.api.withSession


sealed class PagedDocumentPointerReference {
    abstract fun release()
    abstract fun clone(): PagedDocumentPointerReference
    protected abstract fun <R> withPtrs(func: (doc: PointerAccess, ctx: PointerAccess, session: ResourceReference<JavaObjectsRegistrar>?) -> R): R
    protected abstract val owner: TypstCompiler
    abstract fun debugInfo(): ResourceDebugInfo<PointerDebugInfo>


    /**
     * Finish compilation to PNG format.
     *
     * @param from First page index (inclusive).
     * @param to Last page index (exclusive).
     * @param ppi Output resolution in pixels per inch.
     */
    @OptIn(TyKoInternalApi::class)
    fun renderPng(from: Int = 0, to: Int = Int.MAX_VALUE, ppi: Double = 144.0) = withPtrs { doc, ctx, session ->
        owner.bridge.renderPng(doc, from, to, ppi)
            .deserialize<List<Base64Bytes>>()
            .map { it.bytes }
    }


    /**
     * Finish compilation to SVG format.
     *
     * @param from First page index (inclusive).
     * @param to Last page index (exclusive).
     */
    @OptIn(TyKoInternalApi::class)
    fun renderSvg(from: Int = 0, to: Int = Int.MAX_VALUE) = withPtrs { doc, ctx, session ->
        owner.bridge.renderSvg(doc, from, to)
            .deserialize<List<String>>()
    }


    /**
     * Finish compilation to PDF format.
     * This, specifically, can create new errors while enforcing a particular pdf standard.
     * Either returns a pdf, or an error.
     *
     * @param options PDF configuration
     */
    @OptIn(TyKoInternalApi::class)
    fun renderPdfRaw(options: PdfOptionsConfig? = null) = withPtrs { doc, ctx, session ->
        owner.bridge.renderPdf(session, doc, ctx, options?.serialize() ?: "")
            .deserialize<RResult<Base64Bytes, List<SourceDiagnostic>>>()
            .sanitize()
            .map { it.bytes }
    }


    /**
     * Finish compilation to PDF format.
     * This, specifically, can create new errors while enforcing a particular pdf standard.
     * Either returns a pdf, or throws a [TypstCompilerException].
     *
     * @param options Optional PDF serialization options.
     */
    fun renderPdf(options: PdfOptionsConfig? = null) = renderPdfRaw(options).orThrow()

    /**
     * Explicitly set document metadata to have a particular title.
     */
    @OptIn(TyKoInternalApi::class)
    fun enforceTitle(title: String) = withPtrs { doc, ctx, session ->
        owner.bridge.enforceTitle(doc, title)
    }
}

class PagedDocumentPointer(
    internal val pointer: Pointer,
    override val owner: TypstCompiler,
    val carriedContext: FSContextPointerReference,
    internal val carriedSession: ResourceReference<JavaObjectsRegistrar>?
) : PointerWrapper, PagedDocumentPointerReference() {
    override val type: PointerType get() = PointerType.PagedDocument

    init {
        require(pointer.type == type) { "Expected $type pointer, got ${pointer.type}" }
        owner.register(this)
        pointer.addShutdownHook { owner.unregister(this) }
    }

    private inner class Reference(
        val ref: Pointer.Reference,
        val ctxRef: FSContextPointerReference,
        val sessRef: ResourceReference<JavaObjectsRegistrar>?
    ) :
        PagedDocumentPointerReference() {
        override val owner get() = this@PagedDocumentPointer.owner
        override fun release() {
            ref.release()
            ctxRef.release()
            sessRef?.release()
        }

        override fun clone() = Reference(ref.clone(), ctxRef.clone(), sessRef?.reference())
        override fun <R> withPtrs(func: (PointerAccess, PointerAccess, ResourceReference<JavaObjectsRegistrar>?) -> R): R {
            return ref.withPointer(owner.bridge, PointerType.PagedDocument) { doc ->
                ctxRef.withPtr { ctx ->
                    withSession(sessRef) {
                        func(ref, ctx, sessRef)
                    }
                }
            }
        }

        override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = this@PagedDocumentPointer.debugInfo()
    }

    fun reference(): PagedDocumentPointerReference =
        Reference(pointer.reference(), carriedContext.clone(), carriedSession?.reference())

    override fun clone() = reference()
    override fun <R> withPtrs(func: (PointerAccess, PointerAccess, ResourceReference<JavaObjectsRegistrar>?) -> R): R {
        return pointer.withPointer(owner.bridge, PointerType.PagedDocument) { doc ->
            carriedContext.withPtr { ctx ->
                withSession(carriedSession) {
                    func(pointer, ctx, carriedSession)
                }
            }
        }
    }

    override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = pointer.debugInfo()
    override fun release() {
        carriedContext.release()
        pointer.release()
        carriedSession?.release()
    }

    override fun consume(body: (Pointer) -> Unit) {
        try {
            pointer.consume(owner.bridge, type) { body(pointer) }
        } finally {
            carriedContext.release()
            carriedSession?.release()
        }
    }
}
