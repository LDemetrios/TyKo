package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathPrimes(
	val count: TInt,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.primes",
		ArgumentEntry(false, null, count),
	)
	override fun func() = TMathPrimes
    companion object : TElement("math.primes") {
        internal val countType : InternalType = ConcreteType("int")

    }
}

