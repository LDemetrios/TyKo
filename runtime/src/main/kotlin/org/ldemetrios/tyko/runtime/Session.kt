package org.ldemetrios.tyko.runtime

import org.ldemetrios.tyko.driver.api.JavaObjectsRegistrar
import org.ldemetrios.tyko.driver.api.JavaObjectsRegistrarResource
import org.ldemetrios.tyko.driver.api.Resource
import org.ldemetrios.tyko.driver.api.ResourceDebugMode
import org.ldemetrios.tyko.driver.api.withSession
import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.repr

enum class CreateSessionMode {
    Yes, IfNeeded, No
}

internal fun reprWithNewSession(
    value: IntoValue,
    mode: CreateSessionMode,
    debugMode: ResourceDebugMode
): Pair<Resource<JavaObjectsRegistrar>?, String> {
    val session = JavaObjectsRegistrarResource(debugMode)
    val repr = session.withResource { registrar ->
        value.repr { registrar.ticket to registrar.register(it) }
    }
    when (mode) {
        CreateSessionMode.Yes -> return session to repr
        CreateSessionMode.IfNeeded -> {
            if (session.withResource { it.isEmpty() }) {
                session.release()
                return null to repr
            } else {
                return session to repr
            }
        }

        CreateSessionMode.No -> if (session.withResource { it.isEmpty() }) {
            session.release()
            return null to repr
        } else {
            session.release()
            throw IllegalArgumentException("There were java objects in the value, can't `repr` without opened session")
        }
    }
}