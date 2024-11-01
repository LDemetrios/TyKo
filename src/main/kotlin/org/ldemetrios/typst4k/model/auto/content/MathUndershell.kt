package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathUndershell(
	val body: TContent,
	val annotation: TContentOrNone? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.undershell",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, null, annotation),
	)
	override fun func() = TMathUndershell
    companion object : TElement("math.undershell") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val annotationType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))

    }
}

data class TSetMathUndershell(
	val annotation: TContentOrNone? = null,
) : TSetRule("math.undershell") {
    override fun format() = Representations.setRepr(
		"math.undershell",
		ArgumentEntry(false, null, annotation),
	)
}

