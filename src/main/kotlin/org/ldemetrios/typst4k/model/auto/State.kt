package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TState(
	val key: TStr,
	val init: TValue? = null,
) : TValue {
    override fun format() = Representations.structRepr(
		"state",
		ArgumentEntry(false, null, key),
		ArgumentEntry(false, null, init),
	)
	override fun type(): TType = TState
    companion object : TType("state") {
        internal val keyType : InternalType = ConcreteType("str")
        internal val initType : InternalType = AnyType

    }
}
