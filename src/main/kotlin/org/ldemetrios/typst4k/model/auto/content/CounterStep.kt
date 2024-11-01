package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TCounterStep(
	val key: TFunctionOrLabelOrLocationOrSelectorOrStr,
	val level: TInt,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.reprOf(this)
	override fun func() = TCounterStep
    companion object : TElement("counter.step") {
        internal val keyType : InternalType = UnionType(ConcreteType("function"), ConcreteType("label"), ConcreteType("location"), ConcreteType("selector"), ConcreteType("str"))
        internal val levelType : InternalType = ConcreteType("int")

    }
}

