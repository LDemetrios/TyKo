package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TClosure(
	val node: TStr,
	val defaults: TArray<*, >,
	val captured: TDictionary<*, >,
	val numPosParams: TInt,
) : TFunction {
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TClosure
    companion object : TType("closure") {
        internal val nodeType : InternalType = ConcreteType("str")
        internal val defaultsType : InternalType = ConcreteType("array", listOf(AnyType))
        internal val capturedType : InternalType = ConcreteType("dictionary", listOf(AnyType))
        internal val numPosParamsType : InternalType = ConcreteType("int")

    }
}
