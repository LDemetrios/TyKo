package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TStrCounterKey
(
	val str: TStr,
) : TCounterKey{
    override fun format() = Representations.structRepr(
        "str-counter-key",
        ArgumentEntry(false, "str", str),
    )

    companion object  {
        internal val strType : InternalType = ConcreteType("str")

    }
}
