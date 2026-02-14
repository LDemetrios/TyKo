package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.Spacing
import org.ldemetrios.tyko.model.TColorComponent
import org.ldemetrios.tyko.model.TPaint


/**
 * Represents [relative](https://typst.app/docs/reference/layout/relative/) type,
 * which can be either [TLength], [TRatio], or a sum of such.
 */
typealias TRelative = TRelativeBase<*>

//!https://typst.app/docs/reference/layout/relative/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/relative/](https://typst.app/docs/reference/layout/relative/)
 * 
 * A length in relation to some known length.
 * 
 * This type is a combination of a [length](https://typst.app/docs/reference/layout/length/) with a [ratio](https://typst.app/docs/reference/layout/ratio/). It results from addition and subtraction of a length and a ratio. Wherever a relative length is expected, you can also use a bare length or ratio.
 * 
 * **_Relative to the page_**
 * 
 * A common use case is setting the width or height of a layout element (e.g., [block](https://typst.app/docs/reference/layout/block/), [rect](https://typst.app/docs/reference/visualize/rect/), etc.) as a certain percentage of the width of the page. Here, the rectangle's width is set to `25%`, so it takes up one fourth of the page's *inner* width (the width minus margins).
 * 
 * ```typ
 * #rect(width: 25%)
 * ```
 * <img src="https://typst.app/assets/docs/TQDkihTS-mu_3lvYViOhmQAAAAAAAAAA.png" alt="Preview" />
 * 
 * Bare lengths or ratios are always valid where relative lengths are expected, but the two can also be freely mixed:
 * 
 * ```typ
 * #rect(width: 25% + 1cm)
 * ```
 * <img src="https://typst.app/assets/docs/ndZvh4ojAxBp-PA985hy-gAAAAAAAAAA.png" alt="Preview" />
 * 
 * If you're trying to size an element so that it takes up the page's *full* width, you have a few options (this highly depends on your exact use case):
 * 
 * 1. Set page margins to `0pt` (`#set page(margin: 0pt)`)
 * 1. Multiply the ratio by the known full page width (`21cm * 69%`)
 * 1. Use padding which will negate the margins (`#pad(x: -2.5cm, ...)`)
 * 1. Use the page [background](https://typst.app/docs/reference/layout/page/#parameters-background) or [foreground](https://typst.app/docs/reference/layout/page/#parameters-foreground) field as those don't take margins into account (note that it will render the content outside of the document flow, see [place](https://typst.app/docs/reference/layout/place/) to control the content position)
 * 
 * **_Relative to a container_**
 * 
 * When a layout element (e.g. a [rect](https://typst.app/docs/reference/visualize/rect/)) is nested in another layout container (e.g. a [block](https://typst.app/docs/reference/layout/block/)) instead of being a direct descendant of the page, relative widths become relative to the container:
 * 
 * ```typ
 * #block(
 *   width: 100pt,
 *   fill: aqua,
 *   rect(width: 50%),
 * )
 * ```
 * <img src="https://typst.app/assets/docs/QC1YAn5lTiCyNSZEiIb7QAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Scripting_**
 * 
 * You can multiply relative lengths by [ratios](https://typst.app/docs/reference/layout/ratio/), [integers](https://typst.app/docs/reference/foundations/int/), and [floats](https://typst.app/docs/reference/foundations/float/).
 * 
 * A relative length has the following fields:
 * 
 * - `length`: Its [length](https://typst.app/docs/reference/layout/length/) component.
 * - `ratio`: Its [ratio](https://typst.app/docs/reference/layout/ratio/) component.
 * 
 * ```typ
 * #(100% - 50pt).length \
 * #(100% - 50pt).ratio
 * ```
 * <img src="https://typst.app/assets/docs/13CiYHBbJ5ArYeM-jApfKAAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("relative")
open class TRelativeBase<out Self : TRelativeBase<Self>> internal constructor(
    internal val absolutePart: TFloat,
    internal val fontPart: TFloat,
    internal val ratioPart: TFloat,
) : TValue,
    Smart<Self>,
    MarginSplat<Self>,
    CornersSplat<Self>,
    ArrayOrSingle<Self>,
    Option<Self>,
    Spacing,
    Computable<TRelative> {
    override fun type(): TType = TYPE

    /**
     * [TLength] part
     */
    open val length: TLength get() = TLength(absolutePart, fontPart)

    /**
     * [TRatio] part
     */
    open val ratio: TRatio get() = TRatio(ratioPart)

    companion object {
        val TYPE = TType.RELATIVE
    }

    override fun repr(sb: StringBuilder) {
        listOfNotNull(
            "${absolutePart.value}" to "pt",
            "${fontPart.value}" to "em",
            "${ratioPart.value * 100}" to "%",
        )
            .let { it.ifEmpty { listOf(absolutePart.value to "pt") } }
            .joinToString(" + ") { (value, unit) -> "$value$unit" }
            .let { sb.append(it) }
    }
}

//!https://typst.app/docs/reference/layout/length/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/length/](https://typst.app/docs/reference/layout/length/)
 * 
 * A size or distance, possibly expressed with contextual units.
 * 
 * Typst supports the following length units:
 * 
 * - Points: `72pt`
 * - Millimeters: `254mm`
 * - Centimeters: `2.54cm`
 * - Inches: `1in`
 * - Relative to font size: `2.5em`
 * 
 * You can multiply lengths with and divide them by integers and floats.
 * 
 * **_Example_**
 * 
 * ```typ
 * #rect(width: 20pt)
 * #rect(width: 2em)
 * #rect(width: 1in)
 * 
 * #(3em + 5pt).em \
 * #(20pt).em \
 * #(40em + 2pt).abs \
 * #(5em).abs
 * ```
 * <img src="https://typst.app/assets/docs/gpwKHS7y2wIB7BIxGEXoMwAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Fields_**
 * 
 * - `abs`: A length with just the absolute component of the current length (that is, excluding the `em` component).
 * - `em`: The amount of `em` units in this length, as a [float](https://typst.app/docs/reference/foundations/float/).
 */
@SerialName("length")
data class TLength(
    /**
     * Absolute part
     */
    val abs: TFloat,
    /**
     * Font-dependent part
     */
    val em: TFloat
) :
    Smart<TLength>,
    DashLength,
    TRelativeBase<TLength>(
        abs, em, TFloat.ZERO
    ),
    TStroke, TopEdge, BottomEdge, FirstLineIndent {
    override fun type(): TType = TYPE
    override val paint: Smart<TPaint> get() = TAuto
    override val thickness: Smart<TLength> get() = this
    override val cap: Smart<LineCap> get() = TAuto
    override val join: Smart<LineJoin> get() = TAuto
    override val dash: Smart<Dash> get() = TAuto
    override val miterLimit: Smart<TFloat> get() = TAuto
    override val amount: TLength get() = this
    override val all: TBool? get() = null

    override val length: TLength get() = this
    override val ratio: TRatio get() = TRatio.ZERO

    companion object {
        val TYPE = TType.LENGTH
        internal val ZERO = TLength(0.0.t, 0.0.t)
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

//!https://typst.app/docs/reference/layout/ratio/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/ratio/](https://typst.app/docs/reference/layout/ratio/)
 * 
 * A ratio of a whole.
 * 
 * A ratio is written as a number, followed by a percent sign. Ratios most often appear as part of a [relative length](https://typst.app/docs/reference/layout/relative/), to specify the size of some layout element relative to the page or some container.
 * 
 * ```typ
 * #rect(width: 25%)
 * ```
 * <img src="https://typst.app/assets/docs/TQDkihTS-mu_3lvYViOhmQAAAAAAAAAA.png" alt="Preview" />
 * 
 * However, they can also describe any other property that is relative to some base, e.g. an amount of [horizontal scaling](https://typst.app/docs/reference/layout/scale/#parameters-x) or the [height of parentheses](https://typst.app/docs/reference/math/lr/#functions-lr-size) relative to the height of the content they enclose.
 * 
 * **_Scripting_**
 * 
 * Within your own code, you can use ratios as you like. You can multiply them with various other types as shown below:
 * 
 * | Multiply by | Example | Result |
 * | --- | --- | --- |
 * | [`ratio`](https://typst.app/docs/reference/layout/ratio/) | `27% * 10%` | `2.7%` |
 * | [`length`](https://typst.app/docs/reference/layout/length/) | `27% * 100pt` | `27pt` |
 * | [`relative`](https://typst.app/docs/reference/layout/relative/) | `27% * (10% + 100pt)` | `2.7% + 27pt` |
 * | [`angle`](https://typst.app/docs/reference/layout/angle/) | `27% * 100deg` | `27deg` |
 * | [`int`](https://typst.app/docs/reference/foundations/int/) | `27% * 2` | `54%` |
 * | [`float`](https://typst.app/docs/reference/foundations/float/) | `27% * 0.37037` | `10%` |
 * | [`fraction`](https://typst.app/docs/reference/layout/fraction/) | `27% * 3fr` | `0.81fr` |
 * 
 * When ratios are [displayed](https://typst.app/docs/reference/foundations/repr/) in the document, they are rounded to two significant digits for readability.
 */
@SerialName("ratio")
data class TRatio(val value: TFloat) : TValue, TColorComponent, TRelativeBase<TRatio>(
    TFloat.ZERO, TFloat.ZERO, value
) {
    override fun type(): TType = TYPE

    override val length: TLength get() = TLength.ZERO
    override val ratio: TRatio get() = this

    companion object {
        val TYPE = TType.RATIO
        internal val ZERO = TRatio(0.0.t)
    }

    override fun repr(sb: StringBuilder) {
        sb.append(value.value * 100).append("%")
    }
}

/**
 * Converts a float percentage into a ratio value.
 */
val TFloat.pc get() = TRatio((this.value / 100.0).t)

/**
 * Creates an absolute point length from a float value.
 */
val TFloat.pt get() = TLength(this, 0.0.t)
/**
 * Creates an `em` length from a float value.
 */
val TFloat.em get() = TLength(0.0.t, this)

/**
 * Adds two absolute-or-em lengths component-wise.
 */
fun TLength.plus(another: TLength) = TLength(TFloat(abs.value + another.abs.value), TFloat(em.value + another.em.value))
/**
 * Adds two ratio values.
 */
fun TRatio.plus(another: TRatio) = TRatio(TFloat(value.value + another.value.value))
/**
 * Adds two relative values by combining absolute and ratio components.
 */
fun TRelativeBase<*>.plus(another: TRelativeBase<*>): TRelative = TRelativeBase<TRelative>(
    TFloat(absolutePart.value + another.absolutePart.value),
    TFloat(fontPart.value + another.fontPart.value),
    TFloat(ratioPart.value + another.ratioPart.value)
)
