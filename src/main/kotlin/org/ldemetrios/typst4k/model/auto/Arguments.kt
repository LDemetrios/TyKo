package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TArguments<out A : TValue>(
	val positional: TArray<A, >,
	val named: TDictionary<A, >,
) : TValue {
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TArguments
    companion object : TType("arguments") {
        internal fun positionalType(A: InternalType) : InternalType = ConcreteType("array", listOf(A))
        internal fun namedType(A: InternalType) : InternalType = ConcreteType("dictionary", listOf(A))

    }
}
