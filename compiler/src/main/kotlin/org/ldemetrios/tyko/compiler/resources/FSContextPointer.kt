package org.ldemetrios.tyko.compiler

import org.ldemetrios.tyko.driver.api.Pointer
import org.ldemetrios.tyko.driver.api.PointerAccess
import org.ldemetrios.tyko.driver.api.PointerDebugInfo
import org.ldemetrios.tyko.driver.api.PointerType
import org.ldemetrios.tyko.driver.api.ResourceDebugInfo
import org.ldemetrios.tyko.driver.api.Session
import org.ldemetrios.tyko.driver.api.TyKoInternalApi

sealed class FSContextPointerReference {
    abstract fun release()
    abstract fun clone(): FSContextPointerReference
    internal abstract fun <R> withPtr(func: (PointerAccess) -> R): R
    protected abstract val owner: TypstCompiler
    abstract val original: (FileDescriptor, Session?) -> RResult<ByteArray, FileError>
    abstract fun debugInfo(): ResourceDebugInfo<PointerDebugInfo>

    /**
     * Invalidate a specific file in the underlying cache.
     */
    @OptIn(TyKoInternalApi::class)
    fun resetFile(file: FileDescriptor) = withPtr {
        owner.bridge.resetFile(it, file.serialize())
    }
}

/**
 * A virtual file-system context used by compiler operations.
 *
 * Instances manage a native pointer and must be used with the respect to [Pointer] handling rules (most notably, closed in time).
 */
class FSContextPointer(
    internal val pointer: Pointer,
    override val owner: TypstCompiler,
    override val original: (FileDescriptor, Session?) -> RResult<ByteArray, FileError>,
) : PointerWrapper, FSContextPointerReference() {
    override val type: PointerType get() = PointerType.FSContext

    init {
        require(pointer.type == type) { "Expected $type pointer, got ${pointer.type}" }
        owner.register(this)
        pointer.addShutdownHook { owner.unregister(this) }
    }

    internal fun <R> consume(func: (Pointer) -> R): R {
        return pointer.consume(owner.bridge, type) { func(pointer) }
    }

    private inner class Reference(val ref: Pointer.Reference) : FSContextPointerReference() {
        override val owner get() = this@FSContextPointer.owner
        override val original get() = this@FSContextPointer.original
        override fun release() = ref.release()
        override fun clone() = Reference(ref.clone())
        override fun <R> withPtr(func: (PointerAccess) -> R): R {
            return ref.withPointer(owner.bridge, PointerType.FSContext) { func(ref) }
        }
        override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = this@FSContextPointer.debugInfo()
    }

    fun reference(): FSContextPointerReference = Reference(pointer.reference())
    override fun clone() = reference()
    override fun <R> withPtr(func: (PointerAccess) -> R): R {
        return pointer.withPointer(owner.bridge, PointerType.FSContext) { func(pointer) }
    }

    override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = pointer.debugInfo()
    override fun release() = pointer.release()
    override fun consume(body: (Pointer) -> Unit) = pointer.consume(owner.bridge, type) { body(pointer) }
}
