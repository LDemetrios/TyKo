package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TBox(
	val body: TContentOrNone? = null,
	val width: TAutoOrFractionOrRelative? = null,
	val height: TAutoOrRelative? = null,
	val baseline: TRelative? = null,
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val radius: TDictionaryOrRelative<*, >? = null,
	val inset: TDictionaryOrRelative<*, >? = null,
	val outset: TDictionaryOrRelative<*, >? = null,
	val clip: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"box",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "width", width),
		ArgumentEntry(false, "height", height),
		ArgumentEntry(false, "baseline", baseline),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "radius", radius),
		ArgumentEntry(false, "inset", inset),
		ArgumentEntry(false, "outset", outset),
		ArgumentEntry(false, "clip", clip),
	)
	override fun func() = TBox
    companion object : TElement("box") {
        internal val bodyType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val widthType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("relative"))
        internal val heightType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("relative"))
        internal val baselineType : InternalType = ConcreteType("relative")
        internal val fillType : InternalType = UnionType(ConcreteType("color"), ConcreteType("gradient"), ConcreteType("none"), ConcreteType("pattern"))
        internal val strokeType : InternalType = UnionType(ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("none"), ConcreteType("pattern"), ConcreteType("stroke"))
        internal val radiusType : InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))
        internal val insetType : InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))
        internal val outsetType : InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))
        internal val clipType : InternalType = ConcreteType("bool")

    }
}

data class TSetBox(
	val body: TContentOrNone? = null,
	val width: TAutoOrFractionOrRelative? = null,
	val height: TAutoOrRelative? = null,
	val baseline: TRelative? = null,
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val radius: TDictionaryOrRelative<*, >? = null,
	val inset: TDictionaryOrRelative<*, >? = null,
	val outset: TDictionaryOrRelative<*, >? = null,
	val clip: TBool? = null,
) : TSetRule("box") {
    override fun format() = Representations.setRepr(
		"box",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "width", width),
		ArgumentEntry(false, "height", height),
		ArgumentEntry(false, "baseline", baseline),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "radius", radius),
		ArgumentEntry(false, "inset", inset),
		ArgumentEntry(false, "outset", outset),
		ArgumentEntry(false, "clip", clip),
	)
}

