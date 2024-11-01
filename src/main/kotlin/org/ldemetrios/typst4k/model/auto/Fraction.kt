package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TFraction(
	val value: TFloat,
) : TValue, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TAutoOrFractionOrIntOrRelative, 
    TArrayOrFractionOrFunctionOrRelativeOrSides<Nothing, Nothing>, 
    TFractionOrRelative, 
    TAutoOrFractionOrRelative, 
    TContentOrFractionOrRelative, 
    TFractionOrNoneOrRelative {
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TFraction
    companion object : TType("fraction") {
        internal val valueType : InternalType = ConcreteType("float")

    }
}
