package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TTable(
	val children: TArray<TContent>,
	val columns: TArrayOrAutoOrFractionOrIntOrRelative<TAutoOrFractionOrIntOrRelative, >? = null,
	val rows: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val gutter: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val columnGutter: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val rowGutter: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val fill: TArrayOrColorOrFunctionOrGradientOrNoneOrPattern<*, >? = null,
	val align: TAlignmentOrArrayOrAutoOrFunction<TAlignment, >? = null,
	val stroke: TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<*, *, >? = null,
	val inset: TArrayOrFractionOrFunctionOrRelativeOrSides<TFractionOrRelative, TFractionOrRelative, >? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"table",
		ArgumentEntry(true, null, children),
		ArgumentEntry(false, "columns", columns),
		ArgumentEntry(false, "rows", rows),
		ArgumentEntry(false, "gutter", gutter),
		ArgumentEntry(false, "column-gutter", columnGutter),
		ArgumentEntry(false, "row-gutter", rowGutter),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "align", align),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "inset", inset),
	)
	override fun func() = TTable
    companion object : TElement("table") {
        internal val childrenType : InternalType = ConcreteType("array", listOf(ConcreteType("content")))
        internal val columnsType : InternalType = UnionType(ConcreteType("array", listOf(UnionType(ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("int"), ConcreteType("relative")))), ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("int"), ConcreteType("relative"))
        internal val rowsType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("int"), ConcreteType("relative"))
        internal val gutterType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("int"), ConcreteType("relative"))
        internal val columnGutterType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("int"), ConcreteType("relative"))
        internal val rowGutterType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("int"), ConcreteType("relative"))
        internal val fillType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("color"), ConcreteType("function"), ConcreteType("gradient"), ConcreteType("none"), ConcreteType("pattern"))
        internal val alignType : InternalType = UnionType(ConcreteType("alignment"), ConcreteType("array", listOf(ConcreteType("alignment"))), ConcreteType("auto"), ConcreteType("function"))
        internal val strokeType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("function"), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("none"), ConcreteType("pattern"), ConcreteType("stroke"))
        internal val insetType : InternalType = UnionType(ConcreteType("array", listOf(UnionType(ConcreteType("fraction"), ConcreteType("relative")))), ConcreteType("fraction"), ConcreteType("function"), ConcreteType("relative"), ConcreteType("sides", listOf(UnionType(ConcreteType("fraction"), ConcreteType("relative")))))

    }
}

data class TSetTable(
	val columns: TArrayOrAutoOrFractionOrIntOrRelative<TAutoOrFractionOrIntOrRelative, >? = null,
	val rows: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val gutter: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val columnGutter: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val rowGutter: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val fill: TArrayOrColorOrFunctionOrGradientOrNoneOrPattern<*, >? = null,
	val align: TAlignmentOrArrayOrAutoOrFunction<TAlignment, >? = null,
	val stroke: TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<*, *, >? = null,
	val inset: TArrayOrFractionOrFunctionOrRelativeOrSides<TFractionOrRelative, TFractionOrRelative, >? = null,
) : TSetRule("table") {
    override fun format() = Representations.setRepr(
		"table",
		ArgumentEntry(false, "columns", columns),
		ArgumentEntry(false, "rows", rows),
		ArgumentEntry(false, "gutter", gutter),
		ArgumentEntry(false, "column-gutter", columnGutter),
		ArgumentEntry(false, "row-gutter", rowGutter),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "align", align),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "inset", inset),
	)
}

