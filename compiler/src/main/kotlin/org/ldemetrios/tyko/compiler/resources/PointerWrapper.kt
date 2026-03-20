package org.ldemetrios.tyko.compiler

import org.ldemetrios.tyko.compiler.TypstCompiler
import org.ldemetrios.tyko.driver.api.Pointer
import org.ldemetrios.tyko.driver.api.PointerDebugInfo
import org.ldemetrios.tyko.driver.api.PointerType
import org.ldemetrios.tyko.driver.api.ResourceDebugInfo

sealed interface PointerWrapper {
    val type: PointerType

    fun debugInfo(): ResourceDebugInfo<PointerDebugInfo>

    fun release(): Unit

    fun consume(body: (Pointer) -> Unit)
}

