package org.ldemetrios.tyko.runtime

import org.ldemetrios.tyko.compiler.Now
import java.time.Instant

fun buildWorldSpecification(builder: WorldSpecBuilder.() -> Unit): WorldSpecification {
    return WorldSpecBuilder().apply(builder).build()
}

class WorldSpecBuilder {
    private var library = LibrarySpecBuilder()
    private var libTouched = false
    private var fsCtx = FileContextSpecBuilder()
    private var fsTouched = false
    private var fonts = FontsSpecBuilder()
    private var fontsTouched = false
    private var now: Now? = null
    private var nowDefined: Boolean = false
    private var jvmObjectsSession: CreateSessionMode? = null
    private var jvmObjectsSessionDefined: Boolean = false

    private fun defineNow(now: Now?) {
        if (nowDefined) throw IllegalStateException("World time is already specified")
        this.now = now
        nowDefined = true
    }

    private fun defineSession(session: CreateSessionMode?) {
        if (jvmObjectsSessionDefined) throw IllegalStateException("World time is already specified")
        this.jvmObjectsSession = session
        jvmObjectsSessionDefined = true
    }

    fun createSessions(session: CreateSessionMode) {
        defineSession(session)
    }

    fun inheritSessionsMode() {
        defineSession(null)
    }

    fun alwaysCreateSession() = defineSession(CreateSessionMode.Yes)
    fun neverCreateSession() = defineSession(CreateSessionMode.No)
    fun createSessionIfNeeded() = defineSession(CreateSessionMode.IfNeeded)

    fun inheritTime() = defineNow(null)
    fun useSystemTime() = defineNow(Now.System)
    fun forbidTimer() = defineNow(Now.ForbidTimer)
    fun useTime(millis: Long, nanos: Int = 0) = defineNow(Now.Fixed(millis, nanos))
    fun useTime(instant: Instant) = useTime(instant.toEpochMilli(), instant.nano)
    fun useTime(instant: kotlin.time.Instant) = useTime(instant.toEpochMilliseconds(), instant.nanosecondsOfSecond)

    fun library(body: LibrarySpecBuilder.() -> Unit) {
        libTouched = true
        library.body()
    }

    fun library(prototype: LibraryReference) = library { prototype(prototype) }

    fun fonts(body: FontsSpecBuilder.() -> Unit) {
        fontsTouched = true
        fonts.body()
    }

    fun fonts(prototype: FontCollectionReference) {
        fontsTouched = true
        fonts { prototype(prototype) }
    }

    fun files(body: FileContextSpecBuilder.() -> Unit) {
        fsTouched = true
        fsCtx.body()
    }

    fun files(prototype: FSContextReference) {
        fsTouched = true
        files { fallback(prototype.original) }
    }

    internal fun build(): WorldSpecification = WorldSpecification(
        if (libTouched) library.build() else null,
        if (fontsTouched) fonts.build() else null,
        if (fsTouched) fsCtx.build() else null,
        if (nowDefined) now else null,
        if (jvmObjectsSessionDefined) jvmObjectsSession else null,
    )
}
