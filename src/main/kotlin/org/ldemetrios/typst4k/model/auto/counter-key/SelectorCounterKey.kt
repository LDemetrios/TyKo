package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TSelectorCounterKey
(
	val selector: TSelector,
) : TCounterKey{
    override fun format() = Representations.structRepr(
        "selector-counter-key",
        ArgumentEntry(false, "selector", selector),
    )

    companion object  {
        internal val selectorType : InternalType = ConcreteType("selector")

    }
}
