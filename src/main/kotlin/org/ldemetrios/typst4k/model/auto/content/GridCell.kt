package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TGridCell(
	val body: TContent,
	val x: TAutoOrInt? = null,
	val y: TAutoOrInt? = null,
	val colspan: TInt? = null,
	val rowspan: TInt? = null,
	val fill: TAutoOrColorOrGradientOrNoneOrPattern? = null,
	val align: TAlignmentOrAuto? = null,
	val inset: TAutoOrDictionaryOrRelative<*, >? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val breakable: TAutoOrBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"grid.cell",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "x", x),
		ArgumentEntry(false, "y", y),
		ArgumentEntry(false, "colspan", colspan),
		ArgumentEntry(false, "rowspan", rowspan),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "align", align),
		ArgumentEntry(false, "inset", inset),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "breakable", breakable),
	)
	override fun func() = TGridCell
    companion object : TElement("grid.cell") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val xType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("int"))
        internal val yType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("int"))
        internal val colspanType : InternalType = ConcreteType("int")
        internal val rowspanType : InternalType = ConcreteType("int")
        internal val fillType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("color"), ConcreteType("gradient"), ConcreteType("none"), ConcreteType("pattern"))
        internal val alignType : InternalType = UnionType(ConcreteType("alignment"), ConcreteType("auto"))
        internal val insetType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))
        internal val strokeType : InternalType = UnionType(ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("none"), ConcreteType("pattern"), ConcreteType("stroke"))
        internal val breakableType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("bool"))

    }
}

data class TSetGridCell(
	val x: TAutoOrInt? = null,
	val y: TAutoOrInt? = null,
	val colspan: TInt? = null,
	val rowspan: TInt? = null,
	val fill: TAutoOrColorOrGradientOrNoneOrPattern? = null,
	val align: TAlignmentOrAuto? = null,
	val inset: TAutoOrDictionaryOrRelative<*, >? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val breakable: TAutoOrBool? = null,
) : TSetRule("grid.cell") {
    override fun format() = Representations.setRepr(
		"grid.cell",
		ArgumentEntry(false, "x", x),
		ArgumentEntry(false, "y", y),
		ArgumentEntry(false, "colspan", colspan),
		ArgumentEntry(false, "rowspan", rowspan),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "align", align),
		ArgumentEntry(false, "inset", inset),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "breakable", breakable),
	)
}

