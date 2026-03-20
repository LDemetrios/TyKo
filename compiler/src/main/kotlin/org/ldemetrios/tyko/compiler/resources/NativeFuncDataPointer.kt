package org.ldemetrios.tyko.compiler

import org.ldemetrios.tyko.driver.api.Pointer
import org.ldemetrios.tyko.driver.api.PointerAccess
import org.ldemetrios.tyko.driver.api.PointerDebugInfo
import org.ldemetrios.tyko.driver.api.PointerType
import org.ldemetrios.tyko.driver.api.ResourceDebugInfo

sealed class NativeFuncDataPointerReference {
    abstract fun release()
    abstract fun clone(): NativeFuncDataPointerReference
    internal abstract fun <R> withPtr(func: (PointerAccess) -> R): R
    protected abstract val owner: TypstCompiler
    abstract fun debugInfo(): ResourceDebugInfo<PointerDebugInfo>
}

/**
 * A pointer to a `Static<NativeFuncData>` allocated on native side.
 */
class NativeFuncDataPointer(
    internal val pointer: Pointer,
    override val owner: TypstCompiler,
) : PointerWrapper, NativeFuncDataPointerReference() {
    override val type: PointerType get() = PointerType.NativeFuncData

    init {
        require(pointer.type == type) { "Expected $type pointer, got ${pointer.type}" }
        owner.register(this)
        pointer.addShutdownHook { owner.unregister(this) }
    }

    private inner class Reference(val ref: Pointer.Reference) : NativeFuncDataPointerReference() {
        override val owner get() = this@NativeFuncDataPointer.owner
        override fun release() = ref.release()
        override fun clone() = Reference(ref.clone())
        override fun <R> withPtr(func: (PointerAccess) -> R): R {
            return ref.withPointer(owner.bridge, PointerType.NativeFuncData) { func(ref) }
        }

        override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = this@NativeFuncDataPointer.debugInfo()
    }

    fun reference(): NativeFuncDataPointerReference = Reference(pointer.reference())
    override fun clone() = reference()
    override fun <R> withPtr(func: (PointerAccess) -> R): R {
        return pointer.withPointer(owner.bridge, PointerType.NativeFuncData) { func(pointer) }
    }

    override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = pointer.debugInfo()
    override fun release() = pointer.release()

    override fun consume(body: (Pointer) -> Unit) = pointer.consume(owner.bridge, type) { body(pointer) }
}
