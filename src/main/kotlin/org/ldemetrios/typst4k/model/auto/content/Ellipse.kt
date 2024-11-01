package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TEllipse(
	val body: TContentOrNone? = null,
	val width: TAutoOrRelative? = null,
	val height: TAutoOrFractionOrRelative? = null,
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val stroke: TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val inset: TDictionaryOrRelative<*, >? = null,
	val outset: TDictionaryOrRelative<*, >? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"ellipse",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "width", width),
		ArgumentEntry(false, "height", height),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "inset", inset),
		ArgumentEntry(false, "outset", outset),
	)
	override fun func() = TEllipse
    companion object : TElement("ellipse") {
        internal val bodyType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val widthType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("relative"))
        internal val heightType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("relative"))
        internal val fillType : InternalType = UnionType(ConcreteType("color"), ConcreteType("gradient"), ConcreteType("none"), ConcreteType("pattern"))
        internal val strokeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("none"), ConcreteType("pattern"), ConcreteType("stroke"))
        internal val insetType : InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))
        internal val outsetType : InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))

    }
}

data class TSetEllipse(
	val body: TContentOrNone? = null,
	val width: TAutoOrRelative? = null,
	val height: TAutoOrFractionOrRelative? = null,
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val stroke: TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val inset: TDictionaryOrRelative<*, >? = null,
	val outset: TDictionaryOrRelative<*, >? = null,
) : TSetRule("ellipse") {
    override fun format() = Representations.setRepr(
		"ellipse",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "width", width),
		ArgumentEntry(false, "height", height),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "inset", inset),
		ArgumentEntry(false, "outset", outset),
	)
}

