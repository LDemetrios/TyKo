package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathOverparen(
	val body: TContent,
	val annotation: TContentOrNone? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.overparen",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, null, annotation),
	)
	override fun func() = TMathOverparen
    companion object : TElement("math.overparen") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val annotationType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))

    }
}

data class TSetMathOverparen(
	val annotation: TContentOrNone? = null,
) : TSetRule("math.overparen") {
    override fun format() = Representations.setRepr(
		"math.overparen",
		ArgumentEntry(false, null, annotation),
	)
}

