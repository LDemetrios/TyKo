package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathAccent(
	val base: TContent,
	val accent: TContentOrStr,
	val size: TAutoOrRelative? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.accent",
		ArgumentEntry(false, null, base),
		ArgumentEntry(false, null, accent),
		ArgumentEntry(false, "size", size),
	)
	override fun func() = TMathAccent
    companion object : TElement("math.accent") {
        internal val baseType : InternalType = ConcreteType("content")
        internal val accentType : InternalType = UnionType(ConcreteType("content"), ConcreteType("str"))
        internal val sizeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("relative"))

    }
}

data class TSetMathAccent(
	val size: TAutoOrRelative? = null,
) : TSetRule("math.accent") {
    override fun format() = Representations.setRepr(
		"math.accent",
		ArgumentEntry(false, "size", size),
	)
}

