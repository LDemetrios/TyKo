package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TLuma
(
	val lightness: TIntOrRatio,
	val alpha: TRatio? = null,
) : TColor{
    override fun format() = Representations.structRepr(
        "luma",
        ArgumentEntry(false, null, lightness),
        ArgumentEntry(false, null, alpha),
    )

    companion object  {
        internal val lightnessType : InternalType = UnionType(ConcreteType("int"), ConcreteType("ratio"))
        internal val alphaType : InternalType = ConcreteType("ratio")

    }
}
