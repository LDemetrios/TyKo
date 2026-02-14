package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.TPaint

/**
 * Lists supported line cap styles for strokes.
 */
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

/**
 * Lists supported line join styles for strokes.
 */
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

//!https://typst.app/docs/reference/visualize/stroke/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/stroke/](https://typst.app/docs/reference/visualize/stroke/)
 * 
 * Defines how to draw a line.
 * 
 * A stroke has a *paint* (a solid color or gradient), a *thickness,* a line *cap,* a line *join,* a *miter limit,* and a *dash* pattern. All of these values are optional and have sensible defaults.
 * 
 * **_Example_**
 * 
 * ```typ
 * #set line(length: 100%)
 * #stack(
 *   spacing: 1em,
 *   line(stroke: 2pt + red),
 *   line(stroke: (paint: blue, thickness: 4pt, cap: "round")),
 *   line(stroke: (paint: blue, thickness: 1pt, dash: "dashed")),
 *   line(stroke: 2pt + gradient.linear(..color.map.rainbow)),
 * )
 * ```
 * <img src="https://typst.app/assets/docs/3NofubbwIllodsFawlNd8wAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Simple strokes_**
 * 
 * You can create a simple solid stroke from a color, a thickness, or a combination of the two. Specifically, wherever a stroke is expected you can pass any of the following values:
 * 
 * - A length specifying the stroke's thickness. The color is inherited, defaulting to black.
 * - A color to use for the stroke. The thickness is inherited, defaulting to `1pt`.
 * - A stroke combined from color and thickness using the `+` operator as in `2pt + red`.
 * 
 * For full control, you can also provide a [dictionary](https://typst.app/docs/reference/foundations/dictionary/) or a `stroke` object to any function that expects a stroke. The dictionary's keys may include any of the parameters for the constructor function, shown below.
 * 
 * **_Fields_**
 * 
 * On a stroke object, you can access any of the fields listed in the constructor function. For example, `(2pt + blue).thickness` is `2pt`. Meanwhile, `stroke(red).cap` is `auto` because it's unspecified. Fields set to `auto` are inherited.
 */
sealed interface TStroke : TValue {
    override fun type(): TType = TYPE
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/stroke/](https://typst.app/docs/reference/visualize/stroke/)
     *
     * The color or gradient to use for the stroke.
     *
     * If set to `auto`, the value is inherited, defaulting to `black`.
     *
     * Required, positional; Typst type: auto|color|gradient|tiling
     */
    val paint: Smart<TPaint>?
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/stroke/](https://typst.app/docs/reference/visualize/stroke/)
     *
     * The stroke's thickness.
     *
     * If set to `auto`, the value is inherited, defaulting to `1pt`.
     *
     * Required, positional; Typst type: auto|length
     */
    val thickness: Smart<TLength>?
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/stroke/](https://typst.app/docs/reference/visualize/stroke/)
     *
     * How the ends of the stroke are rendered.
     *
     * If set to `auto`, the value is inherited, defaulting to `"butt"`.
     *
     * | Variant | Details |
     * | --- | --- |
     * | `"butt"` | Square stroke cap with the edge at the stroke's end point. |
     * | `"round"` | Circular stroke cap centered at the stroke's end point. |
     * | `"square"` | Square stroke cap centered at the stroke's end point. |
     *
     * Required, positional; Typst type: auto|str
     */
    val cap: Smart<LineCap>?
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/stroke/](https://typst.app/docs/reference/visualize/stroke/)
     *
     * How sharp turns are rendered.
     *
     * If set to `auto`, the value is inherited, defaulting to `"miter"`.
     *
     * | Variant | Details |
     * | --- | --- |
     * | `"miter"` | Segments are joined with sharp edges. Sharp bends exceeding the miter limit are bevelled instead. |
     * | `"round"` | Segments are joined with circular corners. |
     * | `"bevel"` | Segments are joined with a bevel (a straight edge connecting the butts of the joined segments). |
     *
     * Required, positional; Typst type: auto|str
     */
    val join: Smart<LineJoin>?
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/stroke/](https://typst.app/docs/reference/visualize/stroke/)
     *
     * The dash pattern to use. This can be:
     *
     * - One of the predefined patterns:
     *   - `"solid"` or `none`
     *   - `"dotted"`
     *   - `"densely-dotted"`
     *   - `"loosely-dotted"`
     *   - `"dashed"`
     *   - `"densely-dashed"`
     *   - `"loosely-dashed"`
     *   - `"dash-dotted"`
     *   - `"densely-dash-dotted"`
     *   - `"loosely-dash-dotted"`
     * - An [array](https://typst.app/docs/reference/foundations/array/) with alternating lengths for dashes and gaps. You can also use the string `"dot"` for a length equal to the line thickness.
     * - A [dictionary](https://typst.app/docs/reference/foundations/dictionary/) with the keys `array` (same as the array above), and `phase` (of type [length](https://typst.app/docs/reference/layout/length/)), which defines where in the pattern to start drawing.
     *
     * If set to `auto`, the value is inherited, defaulting to `none`.
     *
     * Required, positional; Typst type: none|auto|str|array|dictionary
     */
    val dash: Smart<Dash>?
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/stroke/](https://typst.app/docs/reference/visualize/stroke/)
     *
     * Number at which protruding sharp bends are rendered with a bevel instead or a miter join. The higher the number, the sharper an angle can be before it is bevelled. Only applicable if `join` is `"miter"`.
     *
     * Specifically, the miter limit is the maximum ratio between the corner's protrusion length and the stroke's thickness.
     *
     * If set to `auto`, the value is inherited, defaulting to `4.0`.
     *
     * Required, positional; Typst type: auto|float
     */
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

/**
 * Lists built-in dash pattern presets.
 */
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
