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

/**
 * **_System-wide_** settings for pointer auto-cleanup.
 *
 * **_This DOES NOT enable you to just forget cleanup.
 * There's no guarantee GC will force cleanup even under extreme memory pressure._**
 *
 * This should be used for debugging purposes only (for tracking memory leaks), or as a last, _last_, **_LAST_** resort.
 *
 * To further reinforce that, each property can be no more than once.
 */
public object PointerAutoCleaningConfiguration {
    /**
     * What action will be taken upon forced cleanup. Options: [PointerAutoCleaningMode.Ignore],
     * [PointerAutoCleaningMode.WarnOnly], [PointerAutoCleaningMode.Clean].
     */
    var MODE: PointerAutoCleaningMode by SetOnce(PointerAutoCleaningMode.Ignore)

    /**
     * Whether [Pointer]s should capture the stack trace upon their creation.
     * This can be helpful for debug, but it's extremely expensive mechanism, ergo, disabled by default.
     */
    var CAPTURE_STACK_TRACE: Boolean by SetOnce(false)

    /**
     * Where to issue warnings about unclosed [Pointer]s. The callback accepts options stack trace (if [CAPTURE_STACK_TRACE] is enabled),
     * and a numeric value of the pointer being cleaned.
     */
    var WARNING_CALLBACK by SetOnce<(Array<StackTraceElement>?, Long) -> Unit>({ _, _ -> })
}

/**
 * What action will be taken upon forced cleanup.
 */
enum class PointerAutoCleaningMode {
    /**
     * Do nothing. This is default behaviour.
     */
    Ignore,

    /**
     * Issue a warning into provided callback ([PointerAutoCleaningConfiguration.WARNING_CALLBACK])
     */
    WarnOnly,

    /**
     * Issue a warning and force freeing.
     */
    Clean,
}

private val CLEANER: Cleaner = Cleaner.create()

private val pointers = ConcurrentHashMap<Long, List<WeakReference<Pointer>>>()

/**
 * Returns all unclosed pointers.
 */
@TyKoFFIEntity
fun remainingPointers(): List<Pointer> {
    return pointers.values.flatten().mapNotNull { it.get() }
}

/**
 * This is a handle to a native pointer. It contains, well, the pointer, the driver it originates from,
 * and a native callback to free the memory behind that pointer.
 * The Pointer and its wrappers should be handled as regular resources, that is, not be lost, and be closed in time.
 * They are generally thread-safe (can be acquired in one thread, used in another, freed in the third).
 *
 * Pointer is safe to try to close concurrently: several concurrent `close`s can succeed, but the value is freed exactly once.
 * Consequent attempts to close already closed pointer will result in IllegalStateException.
 *
 * Attempt to use a closed pointer will likely result in IllegalStateException, however, if pointer is closed right at the moment,
 * there's a slight chance [ptr] will return `0` and not an exception. If the pointer is used somewhere in native code,
 * and it is being concurrently freed, there's nothing I can do, you get driver-dependent behaviour
 * (TrapException under Chicory-based driver, rendering that Instance unusable; potential SIGSEGV under Panama-based driver),
 *
 * There's a built-in mechanism that will **_try_** to free forgotten Pointers, but there's no guarantee it will succeed.
 * However, all present Pointers are stored in a weak-ref registry, allowing you to obtain those which weren't disposed.
 * You can enable stack-trace capture and manage other debugging options at [PointerAutoCleaningConfiguration].
 */
@OptIn(TyKoFFIEntity::class)
class Pointer(private val innerPtr: Long, val owner: TypstDriver, private val freer: (Long) -> Unit) : AutoCloseable {
    init {
        pointers.compute(innerPtr) { k, v -> (v ?: listOf()) + WeakReference(this) }
    }

    private val stackTrace = if (PointerAutoCleaningConfiguration.CAPTURE_STACK_TRACE) {
        Thread.currentThread().stackTrace
    } else null

    /**
     * Stack trace pointing to Pointer's creation site, if captured
     */
    val creationStackTrace get() = stackTrace?.toList()

    private var closed = AtomicBoolean(false)
    private val manual = AtomicBoolean(false)
    private var atomicPtr = AtomicLong(innerPtr)

    /**
     * Get the numeric value of the Pointer.
     * Throws IllegalStateException, if the Pointer was already freed.
     * May return `0` if the Pointer is at the moment being freed concurrently.
     */
    val ptr: Long
        get() {
            if (closed.get()) throw IllegalStateException("Pointer is already closed")
            return atomicPtr.get()
        }

    /**
     * Close the Pointer, freeing the memory.
     * Throws IllegalStateException, if the Pointer was already freed.
     * May silently succeed if the Pointer is at the moment being freed concurrently, but the value is freed exactly once regardless.
     */
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

    /**
     * Take the ownership of the Pointer.
     * Throws IllegalStateException, if the Pointer was already freed.
     * May return `0` if the Pointer is at the moment being freed concurrently.
     */
    @TyKoFFIEntity
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

    /**
     * Create a new Pointer with the same owner and freer.
     * If the arguments is the same value as this Pointer,
     * `this` will be returned instead, preventing double-freeing of the pointer.
     * Inherently unsafe, you should verify if the pointer indeed points to analogous value.
     */
    @TyKoFFIEntity
    fun another(ptr: Long) = if (atomicPtr.get() == ptr) this else Pointer(ptr, owner, freer)

    companion object {
        fun nullptr(): Pointer = Pointer(0, NoopDriver) {}
    }
}
