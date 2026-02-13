package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

import kotlin.reflect.KType


sealed interface TCurveControl<out T : IntoValue> : IntoValue {
    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): TCurveControl<T> = when (value) {
            is TArray<*> -> Point.fromValue<T>(value)
            TNone -> TNone
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): TCurveControl<*> = when (value) {
            is TArray<*> -> {
                val arg = type.arguments.firstOrNull()?.type
                if (arg == null) Point.fromValue<IntoValue>(value) else Point.fromValue(value, arg)
            }
            TNone -> TNone
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

//!https://typst.app/docs/reference/visualize/curve/#definitions-cubic
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-cubic](https://typst.app/docs/reference/visualize/curve/#definitions-cubic)
 * 
 * Adds a cubic Bézier curve segment from the last point to `end`, using `control-start` and `control-end` as the control points.
 * 
 * 
 */
@SerialName("curve.cubic")
data class TCurveCubic(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-cubic](https://typst.app/docs/reference/visualize/curve/#definitions-cubic)
     * 
     * The control point going out from the start of the curve segment.
     * 
     * - If `auto` and this element follows another `curve.cubic` element, the last control point will be mirrored. In SVG terms, this makes `curve.cubic` behave like the `S` operator instead of the `C` operator.
     * - If `none`, the curve has no first control point, or equivalently, the control point defaults to the curve's starting point.
     * 
     * Required, positional; Typst type: none|auto|array
     */
    @all:Positional val controlStart: Smart<TCurveControl<TRelative>>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-cubic](https://typst.app/docs/reference/visualize/curve/#definitions-cubic)
     * 
     * The control point going into the end point of the curve segment.
     * 
     * If set to `none`, the curve has no end control point, or equivalently, the control point defaults to the curve's end point.
     * 
     * Required, positional; Typst type: none|array
     */
    @all:Positional val controlEnd: TCurveControl<TRelative>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-cubic](https://typst.app/docs/reference/visualize/curve/#definitions-cubic)
     * 
     * The point at which the curve segment shall end.
     * 
     * Required, positional; Typst type: array
     */
    @all:Positional val end: Point<TRelative>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/curve/#definitions-cubic](https://typst.app/docs/reference/visualize/curve/#definitions-cubic)
     * 
     * Whether the `control-start`, `control-end`, and `end` coordinates are relative to the previous point.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val relative: TBool? = null,
    override val label: TLabel? = null
) : TCurveComponent(label) {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("curve.cubic")
    }
}


@SerialName("set-curve.cubic")
data class TSetCurveCubic(
    override val internals: SetRuleInternals? = null,
    val relative: TBool? = null
) : TSetRule()
