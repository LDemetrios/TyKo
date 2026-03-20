package org.ldemetrios.tyko.runtime

import org.ldemetrios.tyko.compiler.FSContextPointer
import org.ldemetrios.tyko.compiler.FSContextPointerReference
import org.ldemetrios.tyko.compiler.FileDescriptor
import org.ldemetrios.tyko.compiler.FileError
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.driver.api.PointerDebugInfo
import org.ldemetrios.tyko.driver.api.ResourceDebugInfo
import org.ldemetrios.tyko.driver.api.Session


sealed class FSContextReference {
    abstract fun release()
    abstract fun clone(): FSContextReference
    internal abstract fun <R> withPtr(func: (FSContextPointerReference) -> R): R
    protected abstract val owner: TypstRuntime
    abstract fun debugInfo(): ResourceDebugInfo<PointerDebugInfo>
    abstract val original: (FileDescriptor, Session?) -> RResult<ByteArray, FileError>
    fun resetFile(file: FileDescriptor) = withPtr {
        it.resetFile(file)
    }
}

class FSContext(
    internal val pointer: FSContextPointer,
    override val owner: TypstRuntime,
    private val session: Session?,
) : FSContextReference() {
    private inner class Reference(val ref: FSContextPointerReference, val session: Session?) : FSContextReference() {
        override val original: (FileDescriptor, Session?) -> RResult<ByteArray, FileError> = ref.original
        override val owner get() = this@FSContext.owner
        override fun release() {
            ref.release()
            session?.release()
        }

        override fun clone() = Reference(ref.clone(), session?.reference())
        override fun <R> withPtr(func: (FSContextPointerReference) -> R): R {
            return func(ref)
        }

        override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = ref.debugInfo()
    }

    override val original: (FileDescriptor, Session?) -> RResult<ByteArray, FileError> = pointer.original
    fun reference(): FSContextReference = Reference(pointer.reference(), session?.reference())
    override fun clone() = reference()
    override fun <R> withPtr(func: (FSContextPointerReference) -> R): R {
        return func(pointer)
    }

    override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = pointer.debugInfo()
    override fun release() {
        pointer.release()
        session?.release()
    }
}
