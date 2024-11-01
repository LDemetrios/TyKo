package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TNativeFunc(
	val name: TStr,
) : TFunction {
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TNativeFunc
    companion object : TType("native-func") {
        internal val nameType : InternalType = ConcreteType("str")

    }
}
