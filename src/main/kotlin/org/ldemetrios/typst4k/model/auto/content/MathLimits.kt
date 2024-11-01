package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathLimits(
	val body: TContent,
	val inline: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.limits",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "inline", inline),
	)
	override fun func() = TMathLimits
    companion object : TElement("math.limits") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val inlineType : InternalType = ConcreteType("bool")

    }
}

data class TSetMathLimits(
	val inline: TBool? = null,
) : TSetRule("math.limits") {
    override fun format() = Representations.setRepr(
		"math.limits",
		ArgumentEntry(false, "inline", inline),
	)
}

