package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathCancel(
	val body: TContent,
	val length: TRelative? = null,
	val inverted: TBool? = null,
	val cross: TBool? = null,
	val angle: TAngleOrAutoOrFunction? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<*, >? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.cancel",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "length", length),
		ArgumentEntry(false, "inverted", inverted),
		ArgumentEntry(false, "cross", cross),
		ArgumentEntry(false, "angle", angle),
		ArgumentEntry(false, "stroke", stroke),
	)
	override fun func() = TMathCancel
    companion object : TElement("math.cancel") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val lengthType : InternalType = ConcreteType("relative")
        internal val invertedType : InternalType = ConcreteType("bool")
        internal val crossType : InternalType = ConcreteType("bool")
        internal val angleType : InternalType = UnionType(ConcreteType("angle"), ConcreteType("auto"), ConcreteType("function"))
        internal val strokeType : InternalType = UnionType(ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("pattern"), ConcreteType("stroke"))

    }
}

data class TSetMathCancel(
	val length: TRelative? = null,
	val inverted: TBool? = null,
	val cross: TBool? = null,
	val angle: TAngleOrAutoOrFunction? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<*, >? = null,
) : TSetRule("math.cancel") {
    override fun format() = Representations.setRepr(
		"math.cancel",
		ArgumentEntry(false, "length", length),
		ArgumentEntry(false, "inverted", inverted),
		ArgumentEntry(false, "cross", cross),
		ArgumentEntry(false, "angle", angle),
		ArgumentEntry(false, "stroke", stroke),
	)
}

