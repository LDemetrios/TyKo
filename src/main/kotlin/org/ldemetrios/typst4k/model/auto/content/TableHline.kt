package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TTableHline(
	val y: TAutoOrInt? = null,
	val start: TInt? = null,
	val end: TIntOrNone? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val position: TAlignment? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"table.hline",
		ArgumentEntry(false, "y", y),
		ArgumentEntry(false, "start", start),
		ArgumentEntry(false, "end", end),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "position", position),
	)
	override fun func() = TTableHline
    companion object : TElement("table.hline") {
        internal val yType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("int"))
        internal val startType : InternalType = ConcreteType("int")
        internal val endType : InternalType = UnionType(ConcreteType("int"), ConcreteType("none"))
        internal val strokeType : InternalType = UnionType(ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("none"), ConcreteType("pattern"), ConcreteType("stroke"))
        internal val positionType : InternalType = ConcreteType("alignment")

    }
}

data class TSetTableHline(
	val y: TAutoOrInt? = null,
	val start: TInt? = null,
	val end: TIntOrNone? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val position: TAlignment? = null,
) : TSetRule("table.hline") {
    override fun format() = Representations.setRepr(
		"table.hline",
		ArgumentEntry(false, "y", y),
		ArgumentEntry(false, "start", start),
		ArgumentEntry(false, "end", end),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "position", position),
	)
}

