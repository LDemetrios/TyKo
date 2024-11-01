package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathBinom(
	val upper: TContent,
	val lower: TArray<TContent>,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.binom",
		ArgumentEntry(false, null, upper),
		ArgumentEntry(true, null, lower),
	)
	override fun func() = TMathBinom
    companion object : TElement("math.binom") {
        internal val upperType : InternalType = ConcreteType("content")
        internal val lowerType : InternalType = ConcreteType("array", listOf(ConcreteType("content")))

    }
}

