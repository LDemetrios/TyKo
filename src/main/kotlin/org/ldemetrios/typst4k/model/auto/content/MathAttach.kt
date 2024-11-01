package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathAttach(
	val base: TContent,
	val t: TContentOrNone? = null,
	val b: TContentOrNone? = null,
	val tl: TContentOrNone? = null,
	val bl: TContentOrNone? = null,
	val tr: TContentOrNone? = null,
	val br: TContentOrNone? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.attach",
		ArgumentEntry(false, null, base),
		ArgumentEntry(false, "t", t),
		ArgumentEntry(false, "b", b),
		ArgumentEntry(false, "tl", tl),
		ArgumentEntry(false, "bl", bl),
		ArgumentEntry(false, "tr", tr),
		ArgumentEntry(false, "br", br),
	)
	override fun func() = TMathAttach
    companion object : TElement("math.attach") {
        internal val baseType : InternalType = ConcreteType("content")
        internal val tType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val bType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val tlType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val blType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val trType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val brType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))

    }
}

data class TSetMathAttach(
	val t: TContentOrNone? = null,
	val b: TContentOrNone? = null,
	val tl: TContentOrNone? = null,
	val bl: TContentOrNone? = null,
	val tr: TContentOrNone? = null,
	val br: TContentOrNone? = null,
) : TSetRule("math.attach") {
    override fun format() = Representations.setRepr(
		"math.attach",
		ArgumentEntry(false, "t", t),
		ArgumentEntry(false, "b", b),
		ArgumentEntry(false, "tl", tl),
		ArgumentEntry(false, "bl", bl),
		ArgumentEntry(false, "tr", tr),
		ArgumentEntry(false, "br", br),
	)
}

