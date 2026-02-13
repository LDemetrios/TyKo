package org.ldemetrios.tyko.compiler

import kotlinx.serialization.Serializable

@Serializable
enum class SyntaxMode {
    Markup, Code, Math
}