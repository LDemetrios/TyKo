package org.ldemetrios.tyko.runtime

import org.ldemetrios.tyko.compiler.FontCollectionPointer
import org.ldemetrios.tyko.compiler.FontCollectionPointerReference
import org.ldemetrios.tyko.driver.api.PointerDebugInfo
import org.ldemetrios.tyko.driver.api.ResourceDebugInfo

sealed class FontCollectionReference {
    abstract val originalSpec: FontsSpecification

    abstract fun release()
    abstract fun clone(): FontCollectionReference
    internal abstract fun <R> withPtr(func: (FontCollectionPointerReference) -> R): R
    protected abstract val owner: TypstRuntime
    abstract fun debugInfo(): ResourceDebugInfo<PointerDebugInfo>
}

class FontCollection(
    internal val pointer: FontCollectionPointer,
    override val owner: TypstRuntime,
    override val originalSpec: FontsSpecification,
) : FontCollectionReference() {
    private inner class Reference(val ref: FontCollectionPointerReference) : FontCollectionReference() {
        override val originalSpec: FontsSpecification get() = this@FontCollection.originalSpec
        override val owner get() = this@FontCollection.owner
        override fun release() = ref.release()
        override fun clone() = Reference(ref.clone())
        override fun <R> withPtr(func: (FontCollectionPointerReference) -> R): R {
            return func(ref)
        }

        override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = ref.debugInfo()
    }

    fun reference(): FontCollectionReference = Reference(pointer.reference())
    override fun clone() = reference()
    override fun <R> withPtr(func: (FontCollectionPointerReference) -> R): R {
        return func(pointer)
    }

    override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = pointer.debugInfo()
    override fun release() = pointer.release()
}
