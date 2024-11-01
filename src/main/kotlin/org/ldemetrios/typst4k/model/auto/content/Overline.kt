package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TOverline(
	val body: TContent,
	val stroke: TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<*, >? = null,
	val offset: TAutoOrLength? = null,
	val extent: TLength? = null,
	val evade: TBool? = null,
	val background: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"overline",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "offset", offset),
		ArgumentEntry(false, "extent", extent),
		ArgumentEntry(false, "evade", evade),
		ArgumentEntry(false, "background", background),
	)
	override fun func() = TOverline
    companion object : TElement("overline") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val strokeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("pattern"), ConcreteType("stroke"))
        internal val offsetType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("length"))
        internal val extentType : InternalType = ConcreteType("length")
        internal val evadeType : InternalType = ConcreteType("bool")
        internal val backgroundType : InternalType = ConcreteType("bool")

    }
}

data class TSetOverline(
	val stroke: TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<*, >? = null,
	val offset: TAutoOrLength? = null,
	val extent: TLength? = null,
	val evade: TBool? = null,
	val background: TBool? = null,
) : TSetRule("overline") {
    override fun format() = Representations.setRepr(
		"overline",
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "offset", offset),
		ArgumentEntry(false, "extent", extent),
		ArgumentEntry(false, "evade", evade),
		ArgumentEntry(false, "background", background),
	)
}

