package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TPath(
	val vertices: TArray<TArray<*, >>,
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val fillRule: TPathFillRule? = null,
	val stroke: TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val closed: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"path",
		ArgumentEntry(true, null, vertices),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "fill-rule", fillRule),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "closed", closed),
	)
	override fun func() = TPath
    companion object : TElement("path") {
        internal val verticesType : InternalType = ConcreteType("array", listOf(ConcreteType("array", listOf(AnyType))))
        internal val fillType : InternalType = UnionType(ConcreteType("color"), ConcreteType("gradient"), ConcreteType("none"), ConcreteType("pattern"))
        internal val fillRuleType : InternalType = ConcreteType("path-fill-rule")
        internal val strokeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("none"), ConcreteType("pattern"), ConcreteType("stroke"))
        internal val closedType : InternalType = ConcreteType("bool")

    }
}

data class TSetPath(
	val fill: TColorOrGradientOrNoneOrPattern? = null,
	val fillRule: TPathFillRule? = null,
	val stroke: TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val closed: TBool? = null,
) : TSetRule("path") {
    override fun format() = Representations.setRepr(
		"path",
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "fill-rule", fillRule),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "closed", closed),
	)
}

