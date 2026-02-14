package org.ldemetrios.tyko.model

/**
 * Represents possibly named argument of a function. Used to generalize positional and named arguments.
 */
sealed interface Argument

/**
 * Represents a named argument of a function.
 */
data class NamedArgument(val name: String, val value: IntoValue) : Argument

/**
 * Shorthand for constructing a named argument.
 */
infix fun String.eq(value: IntoValue) = NamedArgument(this, value)