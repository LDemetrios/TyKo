package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TDirection(
	val value: TStr,
) : TValue, 
    TAutoOrDirection {
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TDirection
    companion object : TType("direction") {
        internal val valueType : InternalType = ConcreteType("str")

    }
}
