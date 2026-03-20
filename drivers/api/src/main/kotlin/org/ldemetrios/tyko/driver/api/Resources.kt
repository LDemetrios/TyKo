package org.ldemetrios.tyko.driver.api

import java.util.IdentityHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.withLock
import kotlin.concurrent.write

enum class ResourceDebugMode {
    /**
     * Never record stacktraces
     */
    None,

    /**
     * Record stacktrace on resource init
     */
    TrackInit,

    /**
     * Record stacktrace on resource init and all references acquisition
     */
    TrackReferences,

    /**
     * Record stackrace on resource init, all references acquisition and release (means also keeping already released references)
     */
    TrackFull,
}

sealed interface ResourceReference<T : Any> {
    fun <R> withResource(func: (T) -> R): R
    fun release()
    fun reference(): Resource<T>.Reference
    fun debugInfo(): ResourceDebugInfo<T>
}

class Resource<T : Any>(
    private var value: T,
    val freer: (T) -> Unit,
    private val debugMode: ResourceDebugMode
) : ResourceReference<T> {
    private fun stackTraceIfAtLeast(requiredDebugMode: ResourceDebugMode): List<StackTraceElement>? {
        return if (requiredDebugMode.ordinal <= debugMode.ordinal) {
            Thread.currentThread().stackTrace.asList()
        } else {
            null
        }
    }

    private val refLock = ReentrantLock()

    private val usageLock = ReentrantReadWriteLock()

    private var closed: Boolean = false
    private val closingCondition = refLock.newCondition()

    private var consumed: Boolean = false
    private var releaseCondition = refLock.newCondition()

    private val shutdownHooks = ConcurrentLinkedDeque<(T) -> Unit>()

    inner class Reference(val stackTrace: List<StackTraceElement>?) : ResourceReference<T> {
        internal var closed = false
            private set
        internal var releaseStackTrace: List<StackTraceElement>? = null

        override fun release() = release(false)

        override fun reference(): Resource<T>.Reference = clone()

        fun release(dryRun: Boolean) {
            ensureOpen()
            refLock.withLock {
                ensureOpen()
                closed = true
                releaseStackTrace = stackTraceIfAtLeast(ResourceDebugMode.TrackFull)
                if (debugMode == ResourceDebugMode.TrackFull) {
                    releasedReferences[this] = Unit
                }
                references.remove(this)
                releaseCondition.signalAll()
                if (references.isEmpty()) {
                    this@Resource.closed = true
                    if (!dryRun) {
                        freer(value)
                    }
                    shutdownHooks.forEach { it(value) }
                    closingCondition.signalAll()
                }
            }
        }

        fun clone(): Resource<T>.Reference {
            ensureAvailable()
            refLock.withLock {
                ensureAvailable()
                val new = Reference(stackTraceIfAtLeast(ResourceDebugMode.TrackReferences))
                references[new] = Unit
                return new
            }
        }

        override fun <R> withResource(func: (T) -> R): R = usageLock.read {
            val subscription = clone()
            try {
                func(value)
            } finally {
                subscription.release()
            }
        }

        private fun ensureOpen() {
            if (closed) throw IllegalAccessException("This reference is already released")
        }

        private fun ensureAvailable() {
            ensureOpen()
            if (consumed) throw IllegalAccessException("This reference is scheduled for consuming operation")
        }

        override fun debugInfo(): ResourceDebugInfo<T> = this@Resource.debugInfo()
    }

    private var ownedReference = this.Reference(stackTraceIfAtLeast(ResourceDebugMode.TrackInit))

    private var references = IdentityHashMap<Reference, Unit>().also { it[ownedReference] = Unit }
    private val releasedReferences = IdentityHashMap<Reference, Unit>()

    override fun reference() = ownedReference.clone()

    override fun release() = ownedReference.release()

    override fun <R> withResource(func: (T) -> R): R = ownedReference.withResource(func)

    fun <R> consume(func: (T) -> R) = usageLock.write {
        refLock.withLock {
            val subscription = reference() // Prevents from closing
            ownedReference.release() // Prevents from using ownedRef again
            consumed = true
            try {
                while (references.size > 1) { // Wait until other references are released
                    releaseCondition.await()
                }
                func(value)
            } finally {
                subscription.release(dryRun = true)
            }
        }
    }

    fun addShutdownHook(hook: (T) -> Unit) {
        shutdownHooks.add(hook)
    }

    override fun debugInfo(): ResourceDebugInfo<T> = refLock.withLock {
        val keys = (references.keys + releasedReferences.keys - ownedReference)
            .map { ReferenceDebugInfo(it.stackTrace, it.closed, it.releaseStackTrace) }
        ResourceDebugInfo(
            value,
            ReferenceDebugInfo(
                ownedReference.stackTrace,
                ownedReference.closed,
                ownedReference.releaseStackTrace
            ),
            keys
        )
    }
}

data class ResourceDebugInfo<T>(
    val resource: T,
    val ownedReference: ReferenceDebugInfo,
    val others: List<ReferenceDebugInfo>
) {
    fun <R> map(function: (T) -> R) = ResourceDebugInfo(function(resource), ownedReference, others)
}

data class ReferenceDebugInfo(
    val acquisitionStackTrace: List<StackTraceElement>?,
    val released: Boolean,
    val releaseStackTrace: List<StackTraceElement>?,
)
