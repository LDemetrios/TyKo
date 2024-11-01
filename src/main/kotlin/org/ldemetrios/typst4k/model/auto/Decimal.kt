package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TDecimal(
	val value: TStr? = null,
) : TValue {
    override fun format() = Representations.structRepr(
		"decimal",
		ArgumentEntry(false, null, value),
	)
	override fun type(): TType = TDecimal
    companion object : TType("decimal") {
        internal val valueType : InternalType = ConcreteType("str")

    }
}
