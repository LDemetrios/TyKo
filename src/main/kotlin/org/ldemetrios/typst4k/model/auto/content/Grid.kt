package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TGrid(
	val children: TArray<TContent>,
	val columns: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val rows: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val gutter: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val columnGutter: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val rowGutter: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val fill: TArrayOrColorOrFunctionOrGradientOrNoneOrPattern<*, >? = null,
	val align: TAlignmentOrArrayOrAutoOrFunction<TAlignment, >? = null,
	val stroke: TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<*, *, >? = null,
	val inset: TArrayOrDictionaryOrFunctionOrRelative<*, *, >? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"grid",
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
	override fun func() = TGrid
    companion object : TElement("grid") {
        internal val childrenType : InternalType = ConcreteType("array", listOf(ConcreteType("content")))
        internal val columnsType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("int"), ConcreteType("relative"))
        internal val rowsType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("int"), ConcreteType("relative"))
        internal val gutterType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("int"), ConcreteType("relative"))
        internal val columnGutterType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("int"), ConcreteType("relative"))
        internal val rowGutterType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("int"), ConcreteType("relative"))
        internal val fillType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("color"), ConcreteType("function"), ConcreteType("gradient"), ConcreteType("none"), ConcreteType("pattern"))
        internal val alignType : InternalType = UnionType(ConcreteType("alignment"), ConcreteType("array", listOf(ConcreteType("alignment"))), ConcreteType("auto"), ConcreteType("function"))
        internal val strokeType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("function"), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("none"), ConcreteType("pattern"), ConcreteType("stroke"))
        internal val insetType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("function"), ConcreteType("relative"))

    }
}

data class TSetGrid(
	val columns: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val rows: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val gutter: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val columnGutter: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val rowGutter: TArrayOrAutoOrFractionOrIntOrRelative<*, >? = null,
	val fill: TArrayOrColorOrFunctionOrGradientOrNoneOrPattern<*, >? = null,
	val align: TAlignmentOrArrayOrAutoOrFunction<TAlignment, >? = null,
	val stroke: TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<*, *, >? = null,
	val inset: TArrayOrDictionaryOrFunctionOrRelative<*, *, >? = null,
) : TSetRule("grid") {
    override fun format() = Representations.setRepr(
		"grid",
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

