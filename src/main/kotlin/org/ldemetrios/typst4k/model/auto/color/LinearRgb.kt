package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TLinearRgb
(
	val red: TIntOrRatio,
	val green: TIntOrRatio,
	val blue: TIntOrRatio,
	val alpha: TIntOrRatio? = null,
) : TColor{
    override fun format() = Representations.structRepr(
        "color.linear-rgb",
        ArgumentEntry(false, null, red),
        ArgumentEntry(false, null, green),
        ArgumentEntry(false, null, blue),
        ArgumentEntry(false, null, alpha),
    )

    companion object  {
        internal val redType : InternalType = UnionType(ConcreteType("int"), ConcreteType("ratio"))
        internal val greenType : InternalType = UnionType(ConcreteType("int"), ConcreteType("ratio"))
        internal val blueType : InternalType = UnionType(ConcreteType("int"), ConcreteType("ratio"))
        internal val alphaType : InternalType = UnionType(ConcreteType("int"), ConcreteType("ratio"))

    }
}
