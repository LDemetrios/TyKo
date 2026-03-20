package org.ldemetrios.tyko.driver.api


enum class PointerType {
    FSContext,
    FontCollection,
    Library,
    NativeFuncData,
    PagedDocument,
}

interface PointerAccess {
    fun <R> withPointer(sign: TypstCore, expectedType: PointerType, func: (Long) -> R): R
    fun release(): Unit
    fun clone(): PointerAccess
    fun debugInfo(): ResourceDebugInfo<PointerDebugInfo>
}

class Pointer internal constructor(
    private val value: Long,
    private val owner: TypstCore,
    val type: PointerType,
    freer: (Long) -> Unit,
    debugMode: ResourceDebugMode
) : PointerAccess {
    private val resource = Resource(value, freer, debugMode)

    inner class Reference(val resourceRef: Resource<Long>.Reference) : PointerAccess {
        override fun release() = resourceRef.release()
        override fun clone() = Reference(resourceRef.clone())
        override fun <R> withPointer(sign: TypstCore, expectedType: PointerType, func: (Long) -> R): R {
            verify(sign, expectedType)
            return resourceRef.withResource(func)
        }

        override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> = this@Pointer.debugInfo()
    }

    fun reference() = Reference(resource.reference())
    override fun clone() = reference()
    override fun release() = resource.release()
    override fun <R> withPointer(sign: TypstCore, expectedType: PointerType, func: (Long) -> R): R {
        verify(sign, expectedType)
        return resource.withResource(func)
    }

    fun <R> consume(sign: TypstCore, expectedType: PointerType, func: (Long) -> R): R {
        verify(sign, expectedType)
        return resource.consume(func)
    }

    override fun debugInfo(): ResourceDebugInfo<PointerDebugInfo> =
        resource.debugInfo().map { PointerDebugInfo(it, type) }

    private fun verify(sign: TypstCore, expectedType: PointerType) {
        if (value == 0L) throw IllegalArgumentException("Nullptr violates a driver contract. Where did you get it, anyway?")
        if (sign !== owner) throw IllegalArgumentException("Pointers are not interchangeable across cores")
        if (expectedType != type) throw IllegalArgumentException("Expected pointer of type $expectedType, but $type received")
    }

    fun addShutdownHook(hook: () -> Unit) {
        resource.addShutdownHook { hook() }
    }
}

data class PointerDebugInfo(val value: Long, val type: PointerType)