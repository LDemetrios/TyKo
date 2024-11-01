package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathFrac(
	val num: TContent,
	val denom: TContent,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.frac",
		ArgumentEntry(false, null, num),
		ArgumentEntry(false, null, denom),
	)
	override fun func() = TMathFrac
    companion object : TElement("math.frac") {
        internal val numType : InternalType = ConcreteType("content")
        internal val denomType : InternalType = ConcreteType("content")

    }
}

