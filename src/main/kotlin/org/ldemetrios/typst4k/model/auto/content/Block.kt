package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TBlock(
	val body: TContentOrNone? = null,
	val width: TAutoOrRelative? = null,
	val height: TAutoOrFractionOrRelative? = null,
	val breakable: TBool? = null,
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val radius: TDictionaryOrRelative<*, >? = null,
	val inset: TDictionaryOrRelative<*, >? = null,
	val outset: TDictionaryOrRelative<*, >? = null,
	val spacing: TFractionOrRelative? = null,
	val above: TAutoOrFractionOrRelative? = null,
	val below: TAutoOrFractionOrRelative? = null,
	val clip: TBool? = null,
	val sticky: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"block",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "width", width),
		ArgumentEntry(false, "height", height),
		ArgumentEntry(false, "breakable", breakable),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "radius", radius),
		ArgumentEntry(false, "inset", inset),
		ArgumentEntry(false, "outset", outset),
		ArgumentEntry(false, "spacing", spacing),
		ArgumentEntry(false, "above", above),
		ArgumentEntry(false, "below", below),
		ArgumentEntry(false, "clip", clip),
		ArgumentEntry(false, "sticky", sticky),
	)
	override fun func() = TBlock
    companion object : TElement("block") {
        internal val bodyType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val widthType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("relative"))
        internal val heightType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("relative"))
        internal val breakableType : InternalType = ConcreteType("bool")
        internal val fillType : InternalType = UnionType(ConcreteType("color"), ConcreteType("gradient"), ConcreteType("none"), ConcreteType("pattern"))
        internal val strokeType : InternalType = UnionType(ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("none"), ConcreteType("pattern"), ConcreteType("stroke"))
        internal val radiusType : InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))
        internal val insetType : InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))
        internal val outsetType : InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))
        internal val spacingType : InternalType = UnionType(ConcreteType("fraction"), ConcreteType("relative"))
        internal val aboveType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("relative"))
        internal val belowType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("relative"))
        internal val clipType : InternalType = ConcreteType("bool")
        internal val stickyType : InternalType = ConcreteType("bool")

    }
}

data class TSetBlock(
	val body: TContentOrNone? = null,
	val width: TAutoOrRelative? = null,
	val height: TAutoOrFractionOrRelative? = null,
	val breakable: TBool? = null,
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val radius: TDictionaryOrRelative<*, >? = null,
	val inset: TDictionaryOrRelative<*, >? = null,
	val outset: TDictionaryOrRelative<*, >? = null,
	val spacing: TFractionOrRelative? = null,
	val above: TAutoOrFractionOrRelative? = null,
	val below: TAutoOrFractionOrRelative? = null,
	val clip: TBool? = null,
	val sticky: TBool? = null,
) : TSetRule("block") {
    override fun format() = Representations.setRepr(
		"block",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "width", width),
		ArgumentEntry(false, "height", height),
		ArgumentEntry(false, "breakable", breakable),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "radius", radius),
		ArgumentEntry(false, "inset", inset),
		ArgumentEntry(false, "outset", outset),
		ArgumentEntry(false, "spacing", spacing),
		ArgumentEntry(false, "above", above),
		ArgumentEntry(false, "below", below),
		ArgumentEntry(false, "clip", clip),
		ArgumentEntry(false, "sticky", sticky),
	)
}

