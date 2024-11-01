package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

interface TStyle : TValue {
    override fun format() = Representations.structRepr(
		"style",
	)
	override fun type(): TType = TStyle
    companion object : TType("style") {

    }
}
