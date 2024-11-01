package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TSquare(
	val body: TContentOrNone? = null,
	val size: TAutoOrLength? = null,
	val width: TAutoOrRelative? = null,
	val height: TAutoOrFractionOrRelative? = null,
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val stroke: TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val radius: TDictionaryOrRelative<*, >? = null,
	val inset: TDictionaryOrRelative<*, >? = null,
	val outset: TDictionaryOrRelative<*, >? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"square",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "size", size),
		ArgumentEntry(false, "width", width),
		ArgumentEntry(false, "height", height),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "radius", radius),
		ArgumentEntry(false, "inset", inset),
		ArgumentEntry(false, "outset", outset),
	)
	override fun func() = TSquare
    companion object : TElement("square") {
        internal val bodyType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val sizeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("length"))
        internal val widthType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("relative"))
        internal val heightType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("relative"))
        internal val fillType : InternalType = UnionType(ConcreteType("color"), ConcreteType("gradient"), ConcreteType("none"), ConcreteType("pattern"))
        internal val strokeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("none"), ConcreteType("pattern"), ConcreteType("stroke"))
        internal val radiusType : InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))
        internal val insetType : InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))
        internal val outsetType : InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))

    }
}

data class TSetSquare(
	val body: TContentOrNone? = null,
	val size: TAutoOrLength? = null,
	val width: TAutoOrRelative? = null,
	val height: TAutoOrFractionOrRelative? = null,
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val stroke: TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val radius: TDictionaryOrRelative<*, >? = null,
	val inset: TDictionaryOrRelative<*, >? = null,
	val outset: TDictionaryOrRelative<*, >? = null,
) : TSetRule("square") {
    override fun format() = Representations.setRepr(
		"square",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "size", size),
		ArgumentEntry(false, "width", width),
		ArgumentEntry(false, "height", height),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "radius", radius),
		ArgumentEntry(false, "inset", inset),
		ArgumentEntry(false, "outset", outset),
	)
}

