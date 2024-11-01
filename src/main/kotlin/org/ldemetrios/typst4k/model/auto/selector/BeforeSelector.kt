package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TBeforeSelector
(
	val selector: TSelector,
	val end: TSelector,
	val inclusive: TBool? = null,
) : TSelector{
    override fun format() = Representations.reprOf(this)

    companion object  {
        internal val selectorType : InternalType = ConcreteType("selector")
        internal val endType : InternalType = ConcreteType("selector")
        internal val inclusiveType : InternalType = ConcreteType("bool")

    }
}
