package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TSides<out S : TValue>(
	val top: S? = null,
	val right: S? = null,
	val bottom: S? = null,
	val left: S? = null,
) : TDictionary<S>(), 
    TArrayOrFractionOrFunctionOrRelativeOrSides<Nothing, Nothing>, 
    TAutoOrRelativeOrSides<Nothing> {
	override val value: Map<String, S> get() = mapOf(
			"top" to top,
			"right" to right,
			"bottom" to bottom,
			"left" to left,
	).filterValues { it != null }.castUnchecked()
    companion object  {
        internal fun topType(S: InternalType) : InternalType = S
        internal fun rightType(S: InternalType) : InternalType = S
        internal fun bottomType(S: InternalType) : InternalType = S
        internal fun leftType(S: InternalType) : InternalType = S

    }
}

