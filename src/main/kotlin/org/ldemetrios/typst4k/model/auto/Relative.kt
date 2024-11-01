package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

interface TRelative : TValue, 
    TAutoOrBoolOrFunctionOrNoneOrRelative, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TAutoOrFractionOrIntOrRelative, 
    TArrayOrFractionOrFunctionOrRelativeOrSides<Nothing, Nothing>, 
    TFractionOrRelative, 
    TAutoOrRelativeOrSides<Nothing>, 
    TAutoOrRelative, 
    TDictionaryOrRelative<Nothing>, 
    TAutoOrFractionOrRelative, 
    TArrayOrDictionaryOrFunctionOrRelative<Nothing, Nothing>, 
    TAutoOrDictionaryOrRelative<Nothing>, 
    TContentOrFractionOrRelative, 
    TFractionOrNoneOrRelative {
    override fun format() = Representations.structRepr(
		"relative",
	)
	override fun type(): TType = TRelative
    companion object : TType("relative") {

    }
}
