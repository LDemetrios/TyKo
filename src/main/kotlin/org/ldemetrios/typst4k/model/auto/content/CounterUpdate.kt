package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TCounterUpdate(
	val key: TFunctionOrLabelOrLocationOrSelectorOrStr,
	val update: TArrayOrFunctionOrInt<TInt, >,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.reprOf(this)
	override fun func() = TCounterUpdate
    companion object : TElement("counter.update") {
        internal val keyType : InternalType = UnionType(ConcreteType("function"), ConcreteType("label"), ConcreteType("location"), ConcreteType("selector"), ConcreteType("str"))
        internal val updateType : InternalType = UnionType(ConcreteType("array", listOf(ConcreteType("int"))), ConcreteType("function"), ConcreteType("int"))

    }
}

