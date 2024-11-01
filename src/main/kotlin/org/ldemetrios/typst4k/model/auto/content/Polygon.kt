package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TPolygon(
	val vertices: TArray<TArray<*, >>,
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val fillRule: TPolygonFillRule? = null,
	val stroke: TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"polygon",
		ArgumentEntry(true, null, vertices),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "fill-rule", fillRule),
		ArgumentEntry(false, "stroke", stroke),
	)
	override fun func() = TPolygon
    companion object : TElement("polygon") {
        internal val verticesType : InternalType = ConcreteType("array", listOf(ConcreteType("array", listOf(AnyType))))
        internal val fillType : InternalType = UnionType(ConcreteType("color"), ConcreteType("gradient"), ConcreteType("none"), ConcreteType("pattern"))
        internal val fillRuleType : InternalType = ConcreteType("polygon-fill-rule")
        internal val strokeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("none"), ConcreteType("pattern"), ConcreteType("stroke"))

    }
}

data class TSetPolygon(
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val fillRule: TPolygonFillRule? = null,
	val stroke: TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
) : TSetRule("polygon") {
    override fun format() = Representations.setRepr(
		"polygon",
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "fill-rule", fillRule),
		ArgumentEntry(false, "stroke", stroke),
	)
}

