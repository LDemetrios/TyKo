package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

interface TSelector : TValue, 
    TFunctionOrLabelOrLocationOrSelector, 
    TNoneOrSelector, 
    TFunctionOrLabelOrLocationOrSelectorOrStr {
    override fun format() = Representations.structRepr(
		"selector",
	)
	override fun type(): TType = TSelector
    companion object : TType("selector") {

    }
}
