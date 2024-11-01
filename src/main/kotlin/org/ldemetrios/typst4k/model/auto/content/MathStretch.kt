package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathStretch(
	val body: TContent,
	val size: TAutoOrRelative? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.stretch",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "size", size),
	)
	override fun func() = TMathStretch
    companion object : TElement("math.stretch") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val sizeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("relative"))

    }
}

data class TSetMathStretch(
	val size: TAutoOrRelative? = null,
) : TSetRule("math.stretch") {
    override fun format() = Representations.setRepr(
		"math.stretch",
		ArgumentEntry(false, "size", size),
	)
}

