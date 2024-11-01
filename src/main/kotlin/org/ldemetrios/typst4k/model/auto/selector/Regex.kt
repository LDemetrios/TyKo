package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TRegex(
	val regex: TStr? = null,
) : TSelector {
    override fun format() = Representations.structRepr(
		"regex",
		ArgumentEntry(false, null, regex),
	)
	override fun type(): TType = TRegex
    companion object : TType("regex") {
        internal val regexType : InternalType = ConcreteType("str")

    }
}
