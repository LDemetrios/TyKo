package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

typealias TRelative = TRelativeBase<*>

@SerialName("relative")
open class TRelativeBase<out Self : TRelativeBase<Self>>(
    internal val absolutePart: TFloat,
    internal val fontPart: TFloat,
    internal val ratioPart: TFloat,
) : TValue, Smart<Self>, MarginSplat<Self>, CornersSplat<Self>, ArrayOrSingle<Self>, Option<Self>, Spacing,
    Computable<TRelative> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.RELATIVE
    }

    override fun repr(sb: StringBuilder) {
        listOfNotNull(
            "${absolutePart.value}" to "pt",
            "${fontPart.value}" to "em",
            "${ratioPart.value  * 100}" to "%",
        )
            .let { it.ifEmpty { listOf(absolutePart.value to "pt") } }
            .joinToString(" + ") { (value, unit) -> "$value$unit" }
            .let { sb.append(it) }
    }
}


@SerialName("length")
data class TLength(val abs: TFloat, val em: TFloat) : Smart<TLength>, DashLength, TRelativeBase<TLength>(
    abs, em, TFloat.ZERO
), TStroke, TopEdge, BottomEdge, FirstLineIndent {
    override fun type(): TType = TYPE
    override val paint: Smart<TPaint> get() = TAuto
    override val thickness: Smart<TLength> get() = this
    override val cap: Smart<LineCap> get() = TAuto
    override val join: Smart<LineJoin> get() = TAuto
    override val dash: Smart<Dash> get() = TAuto
    override val miterLimit: Smart<TFloat> get() = TAuto
    override val amount: TLength get() = this
    override val all: TBool? get() = null

    companion object {
        val TYPE = TType.LENGTH
    }

    override fun repr(sb: StringBuilder) {
        sb.append("(")
        if (abs.value != 0.0 || em.value == 0.0) {
            sb.append(abs.value).append("pt")
        }
        if (em.value != 0.0 && abs.value != 0.0) {
            sb.append(" + ")
        }
        if (em.value != 0.0) {
            sb.append(em.value).append("em")
        }
        sb.append(")")
    }
}


@SerialName("ratio")
data class TRatio(val value: TFloat) : TValue, TColorComponent, TRelativeBase<TRatio>(
    TFloat.ZERO, TFloat.ZERO, value
) {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.RATIO
    }

    override fun repr(sb: StringBuilder) {
        sb.append(value.value * 100).append("%")
    }
}

val TFloat.pc get() = TRatio((this.value / 100.0).t)

val TFloat.pt get() = TLength(this, 0.0.t)
val TFloat.em get() = TLength(0.0.t, this)

fun TLength.plus(another: TLength) = TLength(TFloat(abs.value + another.abs.value), TFloat(em.value + another.em.value))
fun TRatio.plus(another: TRatio) = TRatio(TFloat(value.value + another.value.value))
fun TRelativeBase<*>.plus(another: TRelativeBase<*>): TRelative = TRelativeBase<TRelative>(
    TFloat(absolutePart.value + another.absolutePart.value),
    TFloat(fontPart.value + another.fontPart.value),
    TFloat(ratioPart.value + another.ratioPart.value)
)
