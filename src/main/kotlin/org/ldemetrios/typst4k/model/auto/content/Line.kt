package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TLine(
	val start: TArray<*, >? = null,
	val end: TArrayOrNone<*, >? = null,
	val length: TRelative? = null,
	val angle: TAngle? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<*, >? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"line",
		ArgumentEntry(false, "start", start),
		ArgumentEntry(false, "end", end),
		ArgumentEntry(false, "length", length),
		ArgumentEntry(false, "angle", angle),
		ArgumentEntry(false, "stroke", stroke),
	)
	override fun func() = TLine
    companion object : TElement("line") {
        internal val startType : InternalType = ConcreteType("array", listOf(AnyType))
        internal val endType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("none"))
        internal val lengthType : InternalType = ConcreteType("relative")
        internal val angleType : InternalType = ConcreteType("angle")
        internal val strokeType : InternalType = UnionType(ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("pattern"), ConcreteType("stroke"))

    }
}

data class TSetLine(
	val start: TArray<*, >? = null,
	val end: TArrayOrNone<*, >? = null,
	val length: TRelative? = null,
	val angle: TAngle? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<*, >? = null,
) : TSetRule("line") {
    override fun format() = Representations.setRepr(
		"line",
		ArgumentEntry(false, "start", start),
		ArgumentEntry(false, "end", end),
		ArgumentEntry(false, "length", length),
		ArgumentEntry(false, "angle", angle),
		ArgumentEntry(false, "stroke", stroke),
	)
}

