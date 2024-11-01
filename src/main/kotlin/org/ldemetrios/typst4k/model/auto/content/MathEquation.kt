package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathEquation(
	val body: TContent,
	val block: TBool? = null,
	val numbering: TFunctionOrNoneOrStr? = null,
	val numberAlign: TAlignment? = null,
	val supplement: TAutoOrContentOrFunctionOrNone? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.equation",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "block", block),
		ArgumentEntry(false, "numbering", numbering),
		ArgumentEntry(false, "number-align", numberAlign),
		ArgumentEntry(false, "supplement", supplement),
	)
	override fun func() = TMathEquation
    companion object : TElement("math.equation") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val blockType : InternalType = ConcreteType("bool")
        internal val numberingType : InternalType = UnionType(ConcreteType("function"), ConcreteType("none"), ConcreteType("str"))
        internal val numberAlignType : InternalType = ConcreteType("alignment")
        internal val supplementType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("content"), ConcreteType("function"), ConcreteType("none"))

    }
}

data class TSetMathEquation(
	val block: TBool? = null,
	val numbering: TFunctionOrNoneOrStr? = null,
	val numberAlign: TAlignment? = null,
	val supplement: TAutoOrContentOrFunctionOrNone? = null,
) : TSetRule("math.equation") {
    override fun format() = Representations.setRepr(
		"math.equation",
		ArgumentEntry(false, "block", block),
		ArgumentEntry(false, "numbering", numbering),
		ArgumentEntry(false, "number-align", numberAlign),
		ArgumentEntry(false, "supplement", supplement),
	)
}

