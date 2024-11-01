package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathVec(
	val children: TArray<TContent>,
	val delim: TArrayOrNoneOrStr<TNoneOrStr, >? = null,
	val align: TAlignment? = null,
	val gap: TRelative? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.vec",
		ArgumentEntry(true, null, children),
		ArgumentEntry(false, "delim", delim),
		ArgumentEntry(false, "align", align),
		ArgumentEntry(false, "gap", gap),
	)
	override fun func() = TMathVec
    companion object : TElement("math.vec") {
        internal val childrenType : InternalType = ConcreteType("array", listOf(ConcreteType("content")))
        internal val delimType : InternalType = UnionType(ConcreteType("array", listOf(UnionType(ConcreteType("none"), ConcreteType("str")))), ConcreteType("none"), ConcreteType("str"))
        internal val alignType : InternalType = ConcreteType("alignment")
        internal val gapType : InternalType = ConcreteType("relative")

    }
}

data class TSetMathVec(
	val delim: TArrayOrNoneOrStr<TNoneOrStr, >? = null,
	val align: TAlignment? = null,
	val gap: TRelative? = null,
) : TSetRule("math.vec") {
    override fun format() = Representations.setRepr(
		"math.vec",
		ArgumentEntry(false, "delim", delim),
		ArgumentEntry(false, "align", align),
		ArgumentEntry(false, "gap", gap),
	)
}

