package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TOklch
(
	val lightness: TRatio,
	val chroma: TFloatOrRatio,
	val hue: TAngle,
	val alpha: TRatio? = null,
) : TColor{
    override fun format() = Representations.structRepr(
        "oklch",
        ArgumentEntry(false, null, lightness),
        ArgumentEntry(false, null, chroma),
        ArgumentEntry(false, null, hue),
        ArgumentEntry(false, null, alpha),
    )

    companion object  {
        internal val lightnessType : InternalType = ConcreteType("ratio")
        internal val chromaType : InternalType = UnionType(ConcreteType("float"), ConcreteType("ratio"))
        internal val hueType : InternalType = ConcreteType("angle")
        internal val alphaType : InternalType = ConcreteType("ratio")

    }
}
