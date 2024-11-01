package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class THsl
(
	val hue: TAngle,
	val saturation: TIntOrRatio,
	val lightness: TIntOrRatio,
	val alpha: TIntOrRatio? = null,
) : TColor{
    override fun format() = Representations.structRepr(
        "color.hsl",
        ArgumentEntry(false, null, hue),
        ArgumentEntry(false, null, saturation),
        ArgumentEntry(false, null, lightness),
        ArgumentEntry(false, null, alpha),
    )

    companion object  {
        internal val hueType : InternalType = ConcreteType("angle")
        internal val saturationType : InternalType = UnionType(ConcreteType("int"), ConcreteType("ratio"))
        internal val lightnessType : InternalType = UnionType(ConcreteType("int"), ConcreteType("ratio"))
        internal val alphaType : InternalType = UnionType(ConcreteType("int"), ConcreteType("ratio"))

    }
}
