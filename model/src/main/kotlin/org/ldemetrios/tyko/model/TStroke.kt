package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

@SerialName("str")
enum class LineCap : IntoStr, Smart<LineCap> {
    @SerialName("butt")
    BUTT,

    @SerialName("round")
    ROUND,

    @SerialName("square")
    SQUARE;

    override fun intoValue(): TStr = TStr(name.lowercase())

    companion object {
        fun fromValue(value: TValue) = when (value) {
            BUTT.intoValue() -> BUTT
            ROUND.intoValue() -> ROUND
            SQUARE.intoValue() -> SQUARE
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@SerialName("str")
enum class LineJoin : IntoStr, Smart<LineJoin> {
    @SerialName("miter")
    MITER,

    @SerialName("round")
    ROUND,

    @SerialName("bevel")
    BEVEL;

    override fun intoValue(): TStr = TStr(name.lowercase())

    companion object {
        fun fromValue(value: TValue) = when (value) {
            MITER.intoValue() -> MITER
            ROUND.intoValue() -> ROUND
            BEVEL.intoValue() -> BEVEL
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}


sealed interface TStroke : TValue {
    override fun type(): TType = TYPE
    val paint: Smart<TPaint>?
    val thickness: Smart<TLength>?
    val cap: Smart<LineCap>?
    val join: Smart<LineJoin>?
    val dash: Smart<Dash>?
    val miterLimit: Smart<TFloat>?

    companion object {
        val TYPE = TType.STROKE
    }
}

@SerialName("stroke")
data class TStrokeImpl(
    override val paint: Smart<TPaint>?,
    override val thickness: Smart<TLength>?,
    override val cap: Smart<LineCap>?,
    override val join: Smart<LineJoin>?,
    override val dash: Smart<Dash>?,
    @all:SerialName("miter-limit") override val miterLimit: Smart<TFloat>?,
) : TValue, TStroke, Option<TStrokeImpl>, Smart<TStrokeImpl>, ArrayOrSingle<TStrokeImpl>, SidesSplat<TStrokeImpl>

fun TStroke(
    paint: Smart<TPaint>,
    thickness: Smart<TLength>,
    cap: Smart<LineCap>,
    join: Smart<LineJoin>,
    dash: Smart<Dash>,
    miterLimit: Smart<TFloat>
) = TStrokeImpl(paint, thickness, cap, join, dash, miterLimit)

sealed interface Dash : IntoValue {
    companion object {
        fun fromValue(value: TValue): Dash = when (value) {
            is TDict<*> -> DashPattern.fromValue(value)
            is TArray<*> -> DashArray.fromValue(value)
            is TStr -> DashPatternPreset.fromValue(value)
            is TNone -> TNone
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@SerialName("array")
data class DashArray(val array: List<DashLength>) : Dash, IntoArr<DashLength>, Smart<DashLength> {
    override fun intoValue(): TArray<DashLength> = TArray(array)

    companion object {
        fun fromValue(value: TValue): DashArray = when (value) {
            is TArray<*> -> DashArray(
                value.map { DashLength.fromValue(it.intoValue()) }
            )
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@SerialName("dict")
data class DashPattern(
    val array: DashArray,
    val phase: TLength
) : Dash, IntoDict<TValue>, Smart<DashPattern> {
    override fun intoValue(): TDict<TValue> = TDict(
        mapOf(
            "array" to array.intoValue(),
            "phase" to phase,
        )
    )

    companion object {
        fun fromValue(value: TValue): DashPattern = when (value) {
            is TDict<*> -> DashPattern(
                DashArray.fromValue(value["array"]!!.intoValue()),
                value["phase"]!!.intoValue() as TLength
            )

            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

sealed interface DashLength : IntoValue {
    companion object {
        fun fromValue(value: TValue) = when (value) {
            is TStr -> when (value.value) {
                "dot" -> LineWidth
                else -> throw AssertionError("Can't convert from $value")
            }

            is TLength -> value
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@SerialName("str")
data object LineWidth : DashLength, IntoStr {
    override fun intoValue(): TStr = TStr("dot")
}

@SerialName("str")
enum class DashPatternPreset(val string: String) : IntoStr, Dash, Smart<DashPatternPreset> {
    SOLID("solid"),
    DOTTED("dotted"),
    DENSELY_DOTTED("densely-dotted"),
    LOOSELY_DOTTED("loosely-dotted"),
    DASHED("dashed"),
    DENSELY_DASHED("densely-dashed"),
    LOOSELY_DASHED("loosely-dashed"),
    DASH("dash-dotted"),
    DENSELY_DASH_DOTTED("densely-dash-dotted"),
    LOOSELY_DASH_DOTTED("loosely-dash-dotted");

    override fun intoValue(): TStr = TStr(string)

    companion object {
        fun fromValue(value: TValue) = when (value) {
            is TStr -> DashPatternPreset.entries.firstOrNull { it.string == value.value }
                ?: throw AssertionError("Can't convert from $value")

            else -> throw AssertionError("Can't convert from $value")
        }
    }
}
