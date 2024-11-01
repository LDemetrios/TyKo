package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TAndSelector
(
	val variants: TArray<TSelector, >,
) : TSelector{
    override fun format() = Representations.reprOf(this)

    companion object  {
        internal val variantsType : InternalType = ConcreteType("array", listOf(ConcreteType("selector")))

    }
}
