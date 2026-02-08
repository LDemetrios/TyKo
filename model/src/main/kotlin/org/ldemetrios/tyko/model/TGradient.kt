package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


sealed class TGradient : TValue, Smart<TGradient>, Option<TGradient>, ArrayOrSingle<TGradient>, TPaint {
    override fun type(): TType = TYPE
    abstract fun func(): TFunc

    companion object {
        val TYPE = TType.GRADIENT
    }
}

@SerialName("array")
data class TGradientStop(val color: TColor, val position: TRatio? = null) : IntoValue {
    constructor(color: TColor, position: TAngle) : this(color, TRatio(TFloat(position.deg.value / 360.0)))

    override fun intoValue(): TValue = if (position == null) color else TArray(listOf(color, position))

    companion object {
        fun fromValue(value: TValue) = when(value) {
            is TColor -> TGradientStop(value)
            is TArray<*> -> if (value.size == 2) {
                TGradientStop(value[0].intoValue() as TColor, value[1].intoValue() as TRatio)
            } else {
                throw AssertionError("Can't convert from $value")
            }
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}


@SerialName("gradient.linear")
data class TLinearGradient(
    @all:Variadic @all:Positional val stops: TArray<TGradientStop>,
    val angle: TAngle? = null,
    @all:Positional val direction: TDirection? = TDirection.LTR,
    val space: TFunc? = null,
    val relative: Smart<PaintRelativeTo>? = TAuto,
) : TGradient() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("gradient.linear")
    }
}


@SerialName("gradient.conic")
data class TConicGradient(
    @all:Variadic @all:Positional val stops: TArray<TGradientStop>,
    val angle: TAngle = 0.0.t.deg,
    val space: TFunc? = null,
    val relative: Smart<PaintRelativeTo>? = TAuto,
    val center: Point<TRatio> = Point(TRatio(0.5.t), TRatio(0.5.t))
) : TGradient() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("gradient.conic")
    }
}


@SerialName("gradient.radial")
data class TRadialGradient(
    @all:Variadic @all:Positional val stops: TArray<TGradientStop>,
    val angle: TAngle? = 0.0.t.deg,
    val space: TFunc? = null,
    val relative: Smart<PaintRelativeTo> = TAuto,
    val center: Point<TRatio> = Point(TRatio(0.5.t), TRatio(0.5.t)),
    val radius: TRatio = TRatio(0.5.t),
    val focalCenter: Smart<Point<TRatio>> = TAuto,
    val focalRadius: TRatio = TRatio(0.0.t)
) : TGradient() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("gradient.radial")
    }
}
