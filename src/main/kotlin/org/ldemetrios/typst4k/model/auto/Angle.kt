package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TAngle(
	val deg: TFloat,
) : TValue, 
    TAngleOrAutoOrFunction {
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TAngle
    companion object : TType("angle") {
        internal val degType : InternalType = ConcreteType("float")

    }
}
