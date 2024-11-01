package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TWith(
	val origin: TFunction,
	val args: TArguments<*, >,
) : TFunction {
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TWith
    companion object : TType("with") {
        internal val originType : InternalType = ConcreteType("function")
        internal val argsType : InternalType = ConcreteType("arguments", listOf(AnyType))

    }
}
