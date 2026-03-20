package org.ldemetrios.tyko.runtime

import org.ldemetrios.tyko.compiler.PagedDocumentPointer
import org.ldemetrios.tyko.compiler.PagedDocumentPointerReference
import org.ldemetrios.tyko.compiler.PdfOptionsConfig
import org.ldemetrios.tyko.compiler.orThrow
import org.ldemetrios.tyko.driver.api.PointerDebugInfo
import org.ldemetrios.tyko.driver.api.ResourceDebugInfo


sealed class PagedDocumentReference {
    abstract fun release()
    abstract fun clone(): PagedDocumentReference
    internal abstract fun <R> withPtr(func: (PagedDocumentPointerReference) -> R): R
    protected abstract val owner: TypstRuntime
    abstract fun debugInfo(): ResourceDebugInfo<PointerDebugInfo>

    fun renderPng(from: Int = 0, to: Int = Int.MAX_VALUE, ppi: Double = 144.0) = withPtr {
        it.renderPng(from, to, ppi)
    }


    fun renderSvg(from: Int = 0, to: Int = Int.MAX_VALUE) = withPtr {
        it.renderSvg(from, to)
    }

    fun renderPdfRaw(options: PdfOptionsConfig? = null) = withPtr {
        it.renderPdfRaw(options)
    }


    fun renderPdf(options: PdfOptionsConfig? = null) = renderPdfRaw(options).orThrow()

    fun enforceTitle(title: String) = withPtr {
        it.enforceTitle(title)
    }
}

class PagedDocument(
    internal val pointer: PagedDocumentPointer,
    override val owner: TypstRuntime,
) : PagedDocumentReference() {
    private inner class Reference(val ref: PagedDocumentPointerReference) : PagedDocumentReference() {
        override val owner get() = this@PagedDocument.owner
        override fun release() = ref.release()
        override fun clone() = Reference(ref.clone())
        override fun <R> withPtr(func: (PagedDocumentPointerReference) -> R): R {
            return func(ref)
        }

        override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = ref.debugInfo()
    }

    fun reference(): PagedDocumentReference = Reference(pointer.reference())
    override fun clone() = reference()
    override fun <R> withPtr(func: (PagedDocumentPointerReference) -> R): R {
        return func(pointer)
    }

    override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = pointer.debugInfo()
    override fun release() = pointer.release()
}
