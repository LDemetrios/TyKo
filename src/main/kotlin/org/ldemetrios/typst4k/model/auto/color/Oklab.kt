package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TOklab
(
	val lightness: TRatio,
	val a: TFloatOrRatio,
	val b: TFloatOrRatio,
	val alpha: TRatio? = null,
) : TColor{
    override fun format() = Representations.structRepr(
        "oklab",
        ArgumentEntry(false, null, lightness),
        ArgumentEntry(false, null, a),
        ArgumentEntry(false, null, b),
        ArgumentEntry(false, null, alpha),
    )

    companion object  {
        internal val lightnessType : InternalType = ConcreteType("ratio")
        internal val aType : InternalType = UnionType(ConcreteType("float"), ConcreteType("ratio"))
        internal val bType : InternalType = UnionType(ConcreteType("float"), ConcreteType("ratio"))
        internal val alphaType : InternalType = ConcreteType("ratio")

    }
}
