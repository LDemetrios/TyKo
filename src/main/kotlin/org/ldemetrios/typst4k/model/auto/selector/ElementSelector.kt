package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TElementSelector
(
	val element: TStr,
	val where: TDictionary<*, >? = null,
) : TSelector{
    override fun format() = Representations.reprOf(this)

    companion object  {
        internal val elementType : InternalType = ConcreteType("str")
        internal val whereType : InternalType = ConcreteType("dictionary", listOf(AnyType))

    }
}
