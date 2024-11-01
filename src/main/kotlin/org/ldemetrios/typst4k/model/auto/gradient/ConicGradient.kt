package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TConicGradient
(
	val stops: TArray<TArrayOrColor<TColorOrRatio, >>,
	val angle: TAngle? = null,
	val relative: TAutoOrStr? = null,
	val center: TArray<TRatio, >? = null,
) : TGradient{
    override fun format() = Representations.structRepr(
        "gradient.conic",
        ArgumentEntry(true, null, stops),
        ArgumentEntry(false, "angle", angle),
        ArgumentEntry(false, "relative", relative),
        ArgumentEntry(false, "center", center),
    )

    companion object  {
        internal val stopsType : InternalType = ConcreteType("array", listOf(UnionType(ConcreteType("array", listOf(UnionType(ConcreteType("color"), ConcreteType("ratio")))), ConcreteType("color"))))
        internal val angleType : InternalType = ConcreteType("angle")
        internal val relativeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("str"))
        internal val centerType : InternalType = ConcreteType("array", listOf(ConcreteType("ratio")))

    }
}
