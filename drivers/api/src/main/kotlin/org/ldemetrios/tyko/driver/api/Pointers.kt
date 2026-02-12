package org.ldemetrios.tyko.driver.api

import java.io.Closeable
import java.io.File
import java.lang.ref.Cleaner
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.system.exitProcess

internal class SetOnce<T : Any>(val default: T) {
    @Volatile
    private var value: T? = null
    private val initialized = AtomicBoolean(false)
    operator fun getValue(holder: Any?, prop: KProperty<*>): T {
        return value ?: default
    }

    operator fun setValue(holder: Any?, prop: KProperty<*>, value: T) {
        if (initialized.getAndSet(true)) {
            throw IllegalStateException("Property ${prop.name} can be set only once")
        }
        this.value = value
    }
}


public object PointerAutoCleaningConfiguration {
    var MODE: PointerAutoCleaningMode by SetOnce(PointerAutoCleaningMode.Ignore)
    var CAPTURE_STACK_TRACE: Boolean by SetOnce(false)
    var WARNING_CALLBACK by SetOnce<(Array<StackTraceElement>?, Long) -> Unit>({ _, _ -> })
}

enum class PointerAutoCleaningMode {
    Ignore, WarnOnly, Clean
}

private val CLEANER: Cleaner = Cleaner.create()

private val pointers = ConcurrentHashMap<Long, List<SoftReference<Pointer>>>()

@Deprecated("This function should only be used for debugging purposes")
fun remainingPointers(): List<Pointer> {
    return pointers.values.flatten().mapNotNull { it.get() }
}

class Pointer(private val innerPtr: Long, val owner: TypstDriver, private val freer: (Long) -> Unit) : Closeable {
    init {
        pointers.compute(innerPtr) { k, v -> (v ?: listOf()) + SoftReference(this) }
    }

    private val stackTrace = if (PointerAutoCleaningConfiguration.CAPTURE_STACK_TRACE) {
        Thread.currentThread().stackTrace
    } else null
    val creationStackTrace get() = stackTrace?.toList()
    private var closed = AtomicBoolean(false)
    private val manual = AtomicBoolean(false)
    private var atomicPtr = AtomicLong(innerPtr)

    val ptr: Long
        get() {
            if (closed.get()) throw IllegalStateException("Pointer is already closed")
            return atomicPtr.get()
        }

    override fun close() {
        unregister()
        manual.set(true)
        when (PointerAutoCleaningConfiguration.MODE) {
            PointerAutoCleaningMode.Ignore -> innerClose()
            PointerAutoCleaningMode.WarnOnly -> {
                cleanable!!.clean() // Omit warning
                innerClose()
            }

            PointerAutoCleaningMode.Clean -> {
                cleanable!!.clean() // Cleanable will close
            }
        }
    }

    private val cleanable =
        if (PointerAutoCleaningConfiguration.MODE != PointerAutoCleaningMode.Ignore) {
            CLEANER.register(this) {
                if (!manual.get()) {
                    PointerAutoCleaningConfiguration.WARNING_CALLBACK.invoke(stackTrace, atomicPtr.get())
                }
                innerClose()
            }
        } else {
            null
        }

    private fun innerClose() {
        val ptr = atomicPtr.getAndSet(0)
        if (ptr == 0L) return // Either it's nullptr, or has been taken
        if (closed.getAndSet(true)) throw IllegalStateException("Pointer is already closed")
        freer(ptr)
    }

    fun take(): Long {
        unregister()
        val ptr = atomicPtr.getAndSet(0L)
        if (closed.getAndSet(true)) throw IllegalStateException("Pointer is already closed")
        return ptr
    }

    private fun unregister() {
        pointers.compute(innerPtr) { k, v ->
            v?.filter { it.get() != this }
        }
    }

    fun another(ptr: Long) = if (atomicPtr.get() == ptr) this else Pointer(ptr, owner, freer)

    companion object {
        fun nullptr(): Pointer = Pointer(0, NoopDriver) {}
    }
}
