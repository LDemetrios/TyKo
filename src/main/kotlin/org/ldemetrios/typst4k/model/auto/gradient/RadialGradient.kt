package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TRadialGradient
(
	val stops: TArray<TArrayOrColor<TColorOrRatio, >>,
	val relative: TAutoOrStr? = null,
	val center: TArray<TRatio, >? = null,
	val radius: TRatio? = null,
	val focalCenter: TArrayOrAuto<TRatio, >? = null,
	val focalRadius: TRatio? = null,
) : TGradient{
    override fun format() = Representations.structRepr(
        "gradient.radial",
        ArgumentEntry(true, null, stops),
        ArgumentEntry(false, "relative", relative),
        ArgumentEntry(false, "center", center),
        ArgumentEntry(false, "radius", radius),
        ArgumentEntry(false, "focal-center", focalCenter),
        ArgumentEntry(false, "focal-radius", focalRadius),
    )

    companion object  {
        internal val stopsType : InternalType = ConcreteType("array", listOf(UnionType(ConcreteType("array", listOf(UnionType(ConcreteType("color"), ConcreteType("ratio")))), ConcreteType("color"))))
        internal val relativeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("str"))
        internal val centerType : InternalType = ConcreteType("array", listOf(ConcreteType("ratio")))
        internal val radiusType : InternalType = ConcreteType("ratio")
        internal val focalCenterType : InternalType = UnionType(ConcreteType("array", listOf(ConcreteType("ratio"))), ConcreteType("auto"))
        internal val focalRadiusType : InternalType = ConcreteType("ratio")

    }
}
