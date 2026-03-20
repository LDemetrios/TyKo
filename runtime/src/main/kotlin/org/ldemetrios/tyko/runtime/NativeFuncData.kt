package org.ldemetrios.tyko.runtime

import org.ldemetrios.tyko.compiler.NativeFuncDataPointer
import org.ldemetrios.tyko.compiler.NativeFuncDataPointerReference
import org.ldemetrios.tyko.driver.api.PointerDebugInfo
import org.ldemetrios.tyko.driver.api.ResourceDebugInfo

sealed class NativeFuncDataReference {
    abstract fun release()
    abstract fun clone(): NativeFuncDataReference
    internal abstract fun <R> withPtr(func: (NativeFuncDataPointerReference) -> R): R
    protected abstract val owner: TypstRuntime
    abstract fun debugInfo(): ResourceDebugInfo<PointerDebugInfo>
}

class NativeFuncData(
    internal val pointer: NativeFuncDataPointer,
    override val owner: TypstRuntime,
) : NativeFuncDataReference() {
    private inner class Reference(val ref: NativeFuncDataPointerReference) : NativeFuncDataReference() {
        override val owner get() = this@NativeFuncData.owner
        override fun release() = ref.release()
        override fun clone() = Reference(ref.clone())
        override fun <R> withPtr(func: (NativeFuncDataPointerReference) -> R): R {
            return func(ref)
        }

        override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = ref.debugInfo()
    }

    fun reference(): NativeFuncDataReference = Reference(pointer.reference())
    override fun clone() = reference()
    override fun <R> withPtr(func: (NativeFuncDataPointerReference) -> R): R {
        return func(pointer)
    }

    override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = pointer.debugInfo()
    override fun release() = pointer.release()
}
