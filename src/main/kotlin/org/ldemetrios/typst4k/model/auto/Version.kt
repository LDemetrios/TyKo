package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TVersion(
	val value: TArray<TInt, >,
) : TValue {
    override fun format() = Representations.structRepr(
		"version",
		ArgumentEntry(false, null, value),
	)
	override fun type(): TType = TVersion
    companion object : TType("version") {
        internal val valueType : InternalType = ConcreteType("array", listOf(ConcreteType("int")))

    }
}
