package org.ldemetrios.tyko.driver.api

import java.lang.IllegalArgumentException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

private val metaregistry = ConcurrentHashMap<Long, JavaObjectsRegistrar>()
private val ticketer = AtomicLong(1)

inline fun <R> withSession(resource: ResourceReference<JavaObjectsRegistrar>?, crossinline body: (Long) -> R): R {
    return if (resource == null) {
        body(0L)
    } else {
        resource.withResource {
            body(it.ticket)
        }
    }
}

typealias Session = ResourceReference<JavaObjectsRegistrar>

fun JavaObjectsRegistrarResource(debugMode: ResourceDebugMode) =
    Resource(JavaObjectsRegistrar(), { it.close() }, debugMode)

class JavaObjectsRegistrar internal constructor() {
    val ticket: Long = ticketer.getAndIncrement()

    init {
        metaregistry[ticket] = this
    }

    private val enumerator = AtomicLong(1)
    private val decoder = ConcurrentHashMap<Long, Any>()
    private val encoder = ConcurrentHashMap<Any, Long>()

    fun isEmpty() = decoder.isEmpty()

    fun register(x: Any?): Long {
        if (x == null) return 0L
        val result = encoder.getOrPut(x) { enumerator.getAndIncrement() }
        decoder.putIfAbsent(result, x)
        return result
    }

    fun resolve(x: Long): Any? {
        return if (x == 0L) null else decoder[x] ?: throw IllegalArgumentException("Unknown object $ticket:$x")
    }

    fun close() {
        metaregistry.remove(ticket)
    }
}

fun resolveJavaObject(session: Long, id: Long): Any? {
    if (id == 0L) return null
    val reg = metaregistry[session] ?: throw IllegalArgumentException("Unknown session $session")
    return reg.resolve(id)
}

fun registerJavaObject(session: Long, x: Any?): Long {
    if (x == null) return 0L
    val reg = metaregistry[session] ?: throw IllegalArgumentException("Unknown session $session")
    return reg.register(x)
}

