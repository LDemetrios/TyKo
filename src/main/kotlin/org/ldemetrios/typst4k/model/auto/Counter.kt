package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TCounter(
	val value: TCounterKey,
) : TValue {
    override fun format() = Representations.structRepr(
		"counter",
		ArgumentEntry(false, null, value),
	)
	override fun type(): TType = TCounter
    companion object : TType("counter") {
        internal val valueType : InternalType = ConcreteType("counter-key")

    }
}
