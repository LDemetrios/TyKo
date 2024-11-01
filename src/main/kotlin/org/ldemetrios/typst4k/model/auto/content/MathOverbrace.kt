package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathOverbrace(
	val body: TContent,
	val annotation: TContentOrNone? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.overbrace",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, null, annotation),
	)
	override fun func() = TMathOverbrace
    companion object : TElement("math.overbrace") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val annotationType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))

    }
}

data class TSetMathOverbrace(
	val annotation: TContentOrNone? = null,
) : TSetRule("math.overbrace") {
    override fun format() = Representations.setRepr(
		"math.overbrace",
		ArgumentEntry(false, null, annotation),
	)
}

