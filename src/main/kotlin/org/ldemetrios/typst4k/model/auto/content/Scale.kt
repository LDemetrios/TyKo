package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TScale(
	val factor: TAutoOrLengthOrRatio? = null,
	val body: TContent,
	val x: TAutoOrLengthOrRatio? = null,
	val y: TAutoOrLengthOrRatio? = null,
	val origin: TAlignment? = null,
	val reflow: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"scale",
		ArgumentEntry(false, null, factor),
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "x", x),
		ArgumentEntry(false, "y", y),
		ArgumentEntry(false, "origin", origin),
		ArgumentEntry(false, "reflow", reflow),
	)
	override fun func() = TScale
    companion object : TElement("scale") {
        internal val factorType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("length"), ConcreteType("ratio"))
        internal val bodyType : InternalType = ConcreteType("content")
        internal val xType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("length"), ConcreteType("ratio"))
        internal val yType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("length"), ConcreteType("ratio"))
        internal val originType : InternalType = ConcreteType("alignment")
        internal val reflowType : InternalType = ConcreteType("bool")

    }
}

data class TSetScale(
	val factor: TAutoOrLengthOrRatio? = null,
	val x: TAutoOrLengthOrRatio? = null,
	val y: TAutoOrLengthOrRatio? = null,
	val origin: TAlignment? = null,
	val reflow: TBool? = null,
) : TSetRule("scale") {
    override fun format() = Representations.setRepr(
		"scale",
		ArgumentEntry(false, null, factor),
		ArgumentEntry(false, "x", x),
		ArgumentEntry(false, "y", y),
		ArgumentEntry(false, "origin", origin),
		ArgumentEntry(false, "reflow", reflow),
	)
}

