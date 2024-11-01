package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathCases(
	val children: TArray<TContent>,
	val delim: TArrayOrNoneOrStr<TNoneOrStr, >? = null,
	val reverse: TBool? = null,
	val gap: TRelative? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.cases",
		ArgumentEntry(true, null, children),
		ArgumentEntry(false, "delim", delim),
		ArgumentEntry(false, "reverse", reverse),
		ArgumentEntry(false, "gap", gap),
	)
	override fun func() = TMathCases
    companion object : TElement("math.cases") {
        internal val childrenType : InternalType = ConcreteType("array", listOf(ConcreteType("content")))
        internal val delimType : InternalType = UnionType(ConcreteType("array", listOf(UnionType(ConcreteType("none"), ConcreteType("str")))), ConcreteType("none"), ConcreteType("str"))
        internal val reverseType : InternalType = ConcreteType("bool")
        internal val gapType : InternalType = ConcreteType("relative")

    }
}

data class TSetMathCases(
	val delim: TArrayOrNoneOrStr<TNoneOrStr, >? = null,
	val reverse: TBool? = null,
	val gap: TRelative? = null,
) : TSetRule("math.cases") {
    override fun format() = Representations.setRepr(
		"math.cases",
		ArgumentEntry(false, "delim", delim),
		ArgumentEntry(false, "reverse", reverse),
		ArgumentEntry(false, "gap", gap),
	)
}

