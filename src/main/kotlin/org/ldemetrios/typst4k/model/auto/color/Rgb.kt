package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TRgb
(
	val hex: TStr,
) : TColor{
    override fun format() = Representations.structRepr(
        "rgb",
        ArgumentEntry(false, null, hex),
    )

    companion object  {
        internal val hexType : InternalType = ConcreteType("str")

    }
}
