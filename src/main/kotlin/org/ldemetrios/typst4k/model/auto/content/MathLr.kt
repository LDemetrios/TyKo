package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathLr(
	val body: TContent,
	val size: TAutoOrRelative? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.lr",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "size", size),
	)
	override fun func() = TMathLr
    companion object : TElement("math.lr") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val sizeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("relative"))

    }
}

data class TSetMathLr(
	val size: TAutoOrRelative? = null,
) : TSetRule("math.lr") {
    override fun format() = Representations.setRepr(
		"math.lr",
		ArgumentEntry(false, "size", size),
	)
}

