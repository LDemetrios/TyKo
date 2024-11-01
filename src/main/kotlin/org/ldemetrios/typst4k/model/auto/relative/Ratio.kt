package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TRatio(
	val value: TFloat,
) : TRelative, 
    TAutoOrLengthOrRatio, 
    TIntOrRatio, 
    TFloatOrRatio, 
    TColorOrRatio {
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TRatio
    companion object : TType("ratio") {
        internal val valueType : InternalType = ConcreteType("float")

    }
}
