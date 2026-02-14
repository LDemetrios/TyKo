package org.ldemetrios.tyko.compiler

import kotlinx.serialization.Serializable

/**
 * How to treat a Typst source: markup (.typ), code (.typc) or math (.typm)
 */
@Serializable
enum class SyntaxMode {
    Markup, Code, Math
}