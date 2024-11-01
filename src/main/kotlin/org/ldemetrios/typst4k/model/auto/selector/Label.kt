package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TLabel(
	val name: TStr,
) : TSelector, 
    TContentOrLabel, 
    TLabelOrLocationOrPositionOrStr, 
    TFunctionOrLabelOrLocationOrSelector, 
    TContentOrLabelOrNone, 
    TFunctionOrLabelOrLocationOrSelectorOrStr {
    override fun format() = Representations.structRepr(
		"label",
		ArgumentEntry(false, null, name),
	)
	override fun type(): TType = TLabel
    companion object : TType("label") {
        internal val nameType : InternalType = ConcreteType("str")

    }
}
