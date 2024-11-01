package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class THighlight(
	val body: TContent,
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val topEdge: THighlightTopEdgeOrLength? = null,
	val bottomEdge: THighlightBottomEdgeOrLength? = null,
	val extent: TLength? = null,
	val radius: TDictionaryOrRelative<*, >? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"highlight",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "top-edge", topEdge),
		ArgumentEntry(false, "bottom-edge", bottomEdge),
		ArgumentEntry(false, "extent", extent),
		ArgumentEntry(false, "radius", radius),
	)
	override fun func() = THighlight
    companion object : TElement("highlight") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val fillType : InternalType = UnionType(ConcreteType("color"), ConcreteType("gradient"), ConcreteType("none"), ConcreteType("pattern"))
        internal val strokeType : InternalType = UnionType(ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("none"), ConcreteType("pattern"), ConcreteType("stroke"))
        internal val topEdgeType : InternalType = UnionType(ConcreteType("highlight-top-edge"), ConcreteType("length"))
        internal val bottomEdgeType : InternalType = UnionType(ConcreteType("highlight-bottom-edge"), ConcreteType("length"))
        internal val extentType : InternalType = ConcreteType("length")
        internal val radiusType : InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))

    }
}

data class TSetHighlight(
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val topEdge: THighlightTopEdgeOrLength? = null,
	val bottomEdge: THighlightBottomEdgeOrLength? = null,
	val extent: TLength? = null,
	val radius: TDictionaryOrRelative<*, >? = null,
) : TSetRule("highlight") {
    override fun format() = Representations.setRepr(
		"highlight",
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "top-edge", topEdge),
		ArgumentEntry(false, "bottom-edge", bottomEdge),
		ArgumentEntry(false, "extent", extent),
		ArgumentEntry(false, "radius", radius),
	)
}

