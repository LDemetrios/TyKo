package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathMat(
	val rows: TArray<TArray<TContent, >>,
	val delim: TArrayOrNoneOrStr<TNoneOrStr, >? = null,
	val align: TAlignment? = null,
	val augment: TDictionaryOrIntOrNone<*, >? = null,
	val gap: TRelative? = null,
	val rowGap: TRelative? = null,
	val columnGap: TRelative? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.mat",
		ArgumentEntry(true, null, rows),
		ArgumentEntry(false, "delim", delim),
		ArgumentEntry(false, "align", align),
		ArgumentEntry(false, "augment", augment),
		ArgumentEntry(false, "gap", gap),
		ArgumentEntry(false, "row-gap", rowGap),
		ArgumentEntry(false, "column-gap", columnGap),
	)
	override fun func() = TMathMat
    companion object : TElement("math.mat") {
        internal val rowsType : InternalType = ConcreteType("array", listOf(ConcreteType("array", listOf(ConcreteType("content")))))
        internal val delimType : InternalType = UnionType(ConcreteType("array", listOf(UnionType(ConcreteType("none"), ConcreteType("str")))), ConcreteType("none"), ConcreteType("str"))
        internal val alignType : InternalType = ConcreteType("alignment")
        internal val augmentType : InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)), ConcreteType("int"), ConcreteType("none"))
        internal val gapType : InternalType = ConcreteType("relative")
        internal val rowGapType : InternalType = ConcreteType("relative")
        internal val columnGapType : InternalType = ConcreteType("relative")

    }
}

data class TSetMathMat(
	val delim: TArrayOrNoneOrStr<TNoneOrStr, >? = null,
	val align: TAlignment? = null,
	val augment: TDictionaryOrIntOrNone<*, >? = null,
	val gap: TRelative? = null,
	val rowGap: TRelative? = null,
	val columnGap: TRelative? = null,
) : TSetRule("math.mat") {
    override fun format() = Representations.setRepr(
		"math.mat",
		ArgumentEntry(false, "delim", delim),
		ArgumentEntry(false, "align", align),
		ArgumentEntry(false, "augment", augment),
		ArgumentEntry(false, "gap", gap),
		ArgumentEntry(false, "row-gap", rowGap),
		ArgumentEntry(false, "column-gap", columnGap),
	)
}

