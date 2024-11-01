package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

interface TCounterKey : TValue {
    override fun format() = Representations.structRepr(
		"counter-key",
	)
	override fun type(): TType = TCounterKey
    companion object : TType("counter-key") {

    }
}
