package org.ldemetrios.tyko.model

//!https://typst.app/docs/reference/visualize/gradient/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/gradient/](https://typst.app/docs/reference/visualize/gradient/)
 * 
 * A color gradient.
 * 
 * Typst supports linear gradients through the [`gradient.linear` function](https://typst.app/docs/reference/visualize/gradient/#definitions-linear), radial gradients through the [`gradient.radial` function](https://typst.app/docs/reference/visualize/gradient/#definitions-radial), and conic gradients through the [`gradient.conic` function](https://typst.app/docs/reference/visualize/gradient/#definitions-conic).
 * 
 * A gradient can be used for the following purposes:
 * 
 * - As a fill to paint the interior of a shape: `rect(fill: gradient.linear(..))`
 * - As a stroke to paint the outline of a shape: `rect(stroke: 1pt + gradient.linear(..))`
 * - As the fill of text: `set text(fill: gradient.linear(..))`
 * - As a color map you can [sample](https://typst.app/docs/reference/visualize/gradient/#definitions-sample) from: `gradient.linear(..).sample(50%)`
 * 
 * **_Examples_**
 * 
 * ```typ
 * #stack(
 *   dir: ltr,
 *   spacing: 1fr,
 *   square(fill: gradient.linear(..color.map.rainbow)),
 *   square(fill: gradient.radial(..color.map.rainbow)),
 *   square(fill: gradient.conic(..color.map.rainbow)),
 * )
 * ```
 * <img src="https://typst.app/assets/docs/_ynuy5GKkV7ADtX87C9EiAAAAAAAAAAA.png" alt="Preview" />
 * 
 * Gradients are also supported on text, but only when setting the [relativeness](https://typst.app/docs/reference/visualize/gradient/#definitions-relative) to either `auto` (the default value) or `"parent"`. To create word-by-word or glyph-by-glyph gradients, you can wrap the words or characters of your text in [boxes](https://typst.app/docs/reference/layout/box/) manually or through a [show rule](https://typst.app/docs/reference/styling/#show-rules).
 * 
 * ```typ
 * #set text(fill: gradient.linear(red, blue))
 * #let rainbow(content) = {
 *   set text(fill: gradient.linear(..color.map.rainbow))
 *   box(content)
 * }
 * 
 * This is a gradient on text, but with a #rainbow[twist]!
 * ```
 * <img src="https://typst.app/assets/docs/ch0LALUCwuQoVDnxrE_UZwAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Stops_**
 * 
 * A gradient is composed of a series of stops. Each of these stops has a color and an offset. The offset is a [ratio](https://typst.app/docs/reference/layout/ratio/) between `0%` and `100%` or an angle between `0deg` and `360deg`. The offset is a relative position that determines how far along the gradient the stop is located. The stop's color is the color of the gradient at that position. You can choose to omit the offsets when defining a gradient. In this case, Typst will space all stops evenly.
 * 
 * Typst predefines color maps that you can use as stops. See the [`color`](https://typst.app/docs/reference/visualize/color/#predefined-color-maps) documentation for more details.
 * 
 * **_Relativeness_**
 * 
 * The location of the `0%` and `100%` stops depends on the dimensions of a container. This container can either be the shape that it is being painted on, or the closest surrounding container. This is controlled by the `relative` argument of a gradient constructor. By default, gradients are relative to the shape they are being painted on, unless the gradient is applied on text, in which case they are relative to the closest ancestor container.
 * 
 * Typst determines the ancestor container as follows:
 * 
 * - For shapes that are placed at the root/top level of the document, the closest ancestor is the page itself.
 * - For other shapes, the ancestor is the innermost [`block`](https://typst.app/docs/reference/layout/block/) or [`box`](https://typst.app/docs/reference/layout/box/) that contains the shape. This includes the boxes and blocks that are implicitly created by show rules and elements. For example, a [`rotate`](https://typst.app/docs/reference/layout/rotate/) will not affect the parent of a gradient, but a [`grid`](https://typst.app/docs/reference/layout/grid/) will.
 * 
 * **_Color spaces and interpolation_**
 * 
 * Gradients can be interpolated in any color space. By default, gradients are interpolated in the [Oklab](https://typst.app/docs/reference/visualize/color/#definitions-oklab) color space, which is a [perceptually uniform](https://programmingdesignsystems.com/color/perceptually-uniform-color-spaces/index.html) color space. This means that the gradient will be perceived as having a smooth progression of colors. This is particularly useful for data visualization.
 * 
 * However, you can choose to interpolate the gradient in any supported color space you want, but beware that some color spaces are not suitable for perceptually interpolating between colors. Consult the table below when choosing an interpolation space.
 * 
 * | Color space | Perceptually uniform? |
 * | --- | --- |
 * | [Oklab](https://typst.app/docs/reference/visualize/color/#definitions-oklab) | *Yes* |
 * | [Oklch](https://typst.app/docs/reference/visualize/color/#definitions-oklch) | *Yes* |
 * | [sRGB](https://typst.app/docs/reference/visualize/color/#definitions-rgb) | *No* |
 * | [linear-RGB](https://typst.app/docs/reference/visualize/color/#definitions-linear-rgb) | *Yes* |
 * | [CMYK](https://typst.app/docs/reference/visualize/color/#definitions-cmyk) | *No* |
 * | [Grayscale](https://typst.app/docs/reference/visualize/color/#definitions-luma) | *Yes* |
 * | [HSL](https://typst.app/docs/reference/visualize/color/#definitions-hsl) | *No* |
 * | [HSV](https://typst.app/docs/reference/visualize/color/#definitions-hsv) | *No* |
 * 
 * <img src="https://typst.app/assets/docs/vhi2AIjx3T8urqy_40DDsQAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Direction_**
 * 
 * Some gradients are sensitive to direction. For example, a linear gradient has an angle that determines its direction. Typst uses a clockwise angle, with 0° being from left to right, 90° from top to bottom, 180° from right to left, and 270° from bottom to top.
 * 
 * ```typ
 * #stack(
 *   dir: ltr,
 *   spacing: 1fr,
 *   square(fill: gradient.linear(red, blue, angle: 0deg)),
 *   square(fill: gradient.linear(red, blue, angle: 90deg)),
 *   square(fill: gradient.linear(red, blue, angle: 180deg)),
 *   square(fill: gradient.linear(red, blue, angle: 270deg)),
 * )
 * ```
 * <img src="https://typst.app/assets/docs/cXgxeaTP2ci7NL16a3rB_gAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Note on file sizes_**
 * 
 * Gradients can be quite large, especially if they have many stops. This is because gradients are stored as a list of colors and offsets, which can take up a lot of space. If you are concerned about file sizes, you should consider the following:
 * 
 * - SVG gradients are currently inefficiently encoded. This will be improved in the future.
 * - PDF gradients in the [`color.oklab`](https://typst.app/docs/reference/visualize/color/#definitions-oklab), [`color.hsv`](https://typst.app/docs/reference/visualize/color/#definitions-hsv), [`color.hsl`](https://typst.app/docs/reference/visualize/color/#definitions-hsl), and [`color.oklch`](https://typst.app/docs/reference/visualize/color/#definitions-oklch) color spaces are stored as a list of [`color.rgb`](https://typst.app/docs/reference/visualize/color/#definitions-rgb) colors with extra stops in between. This avoids needing to encode these color spaces in your PDF file, but it does add extra stops to your gradient, which can increase the file size.
 */
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


//!https://typst.app/docs/reference/visualize/gradient/#definitions-linear
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-linear](https://typst.app/docs/reference/visualize/gradient/#definitions-linear)
 * 
 * Creates a new linear gradient, in which colors transition along a straight line.
 * 
 * 
 */
@SerialName("gradient.linear")
data class TLinearGradient(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-linear](https://typst.app/docs/reference/visualize/gradient/#definitions-linear)
     * 
     * The color [stops](https://typst.app/docs/reference/visualize/gradient/#stops) of the gradient.
     * 
     * Required, positional, variadic; Typst type: color|array
     */
    @all:Variadic @all:Positional val stops: TArray<TGradientStop>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-linear](https://typst.app/docs/reference/visualize/gradient/#definitions-linear)
     * 
     * The angle of the gradient.
     * 
     * Required, positional; Typst type: angle
     */
    val angle: TAngle? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-linear](https://typst.app/docs/reference/visualize/gradient/#definitions-linear)
     * 
     * The direction of the gradient.
     * 
     * Positional; Typst type: direction
     */
    @all:Positional val dir: TDirection? = TDirection.LTR,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-linear](https://typst.app/docs/reference/visualize/gradient/#definitions-linear)
     * 
     * The color space in which to interpolate the gradient.
     * 
     * Defaults to a perceptually uniform color space called [Oklab](https://typst.app/docs/reference/visualize/color/#definitions-oklab).
     * 
     * Typst type: any
     */
    val space: TFunc? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-linear](https://typst.app/docs/reference/visualize/gradient/#definitions-linear)
     * 
     * The [relative placement](https://typst.app/docs/reference/visualize/gradient/#relativeness) of the gradient.
     * 
     * For an element placed at the root/top level of the document, the parent is the page itself. For other elements, the parent is the innermost block, box, column, grid, or stack that contains the element.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"self"` | Relative to itself (its own bounding box). |
     * | `"parent"` | Relative to its parent (the parent's bounding box). |
     * 
     * Typst type: auto|str
     */
    val relative: Smart<PaintRelativeTo>? = TAuto,
) : TGradient() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("gradient.linear")
    }
}

//!https://typst.app/docs/reference/visualize/gradient/#definitions-conic
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-conic](https://typst.app/docs/reference/visualize/gradient/#definitions-conic)
 * 
 * Creates a new conic gradient, in which colors change radially around a center point.
 * 
 * You can control the center point of the gradient by using the `center` argument. By default, the center point is the center of the shape.
 * 
 * 
 */
@SerialName("gradient.conic")
data class TConicGradient(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-conic](https://typst.app/docs/reference/visualize/gradient/#definitions-conic)
     * 
     * The color [stops](https://typst.app/docs/reference/visualize/gradient/#stops) of the gradient.
     * 
     * Required, positional, variadic; Typst type: color|array
     */
    @all:Variadic @all:Positional val stops: TArray<TGradientStop>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-conic](https://typst.app/docs/reference/visualize/gradient/#definitions-conic)
     * 
     * The angle of the gradient.
     * 
     * Typst type: angle
     */
    val angle: TAngle = 0.0.t.deg,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-conic](https://typst.app/docs/reference/visualize/gradient/#definitions-conic)
     * 
     * The color space in which to interpolate the gradient.
     * 
     * Defaults to a perceptually uniform color space called [Oklab](https://typst.app/docs/reference/visualize/color/#definitions-oklab).
     * 
     * Typst type: any
     */
    val space: TFunc? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-conic](https://typst.app/docs/reference/visualize/gradient/#definitions-conic)
     * 
     * The [relative placement](https://typst.app/docs/reference/visualize/gradient/#relativeness) of the gradient.
     * 
     * For an element placed at the root/top level of the document, the parent is the page itself. For other elements, the parent is the innermost block, box, column, grid, or stack that contains the element.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"self"` | Relative to itself (its own bounding box). |
     * | `"parent"` | Relative to its parent (the parent's bounding box). |
     * 
     * Typst type: auto|str
     */
    val relative: Smart<PaintRelativeTo>? = TAuto,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-conic](https://typst.app/docs/reference/visualize/gradient/#definitions-conic)
     * 
     * The center of the circle of the gradient.
     * 
     * A value of `(50%, 50%)` means that the circle is centered inside of its container.
     * 
     * Typst type: array
     */
    val center: TPoint<TRatio> = TPoint(TRatio(0.5.t), TRatio(0.5.t))
) : TGradient() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("gradient.conic")
    }
}

//!https://typst.app/docs/reference/visualize/gradient/#definitions-radial
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-radial](https://typst.app/docs/reference/visualize/gradient/#definitions-radial)
 * 
 * Creates a new radial gradient, in which colors radiate away from an origin.
 * 
 * The gradient is defined by two circles: the focal circle and the end circle. The focal circle is a circle with center `focal-center` and radius `focal-radius`, that defines the points at which the gradient starts and has the color of the first stop. The end circle is a circle with center `center` and radius `radius`, that defines the points at which the gradient ends and has the color of the last stop. The gradient is then interpolated between these two circles.
 * 
 * Using these four values, also called the focal point for the starting circle and the center and radius for the end circle, we can define a gradient with more interesting properties than a basic radial gradient.
 * 
 * 
 */
@SerialName("gradient.radial")
data class TRadialGradient(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-radial](https://typst.app/docs/reference/visualize/gradient/#definitions-radial)
     * 
     * The color [stops](https://typst.app/docs/reference/visualize/gradient/#stops) of the gradient.
     * 
     * Required, positional, variadic; Typst type: color|array
     */
    @all:Variadic @all:Positional val stops: TArray<TGradientStop>,
    val angle: TAngle? = 0.0.t.deg,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-radial](https://typst.app/docs/reference/visualize/gradient/#definitions-radial)
     * 
     * The color space in which to interpolate the gradient.
     * 
     * Defaults to a perceptually uniform color space called [Oklab](https://typst.app/docs/reference/visualize/color/#definitions-oklab).
     * 
     * Typst type: any
     */
    val space: TFunc? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-radial](https://typst.app/docs/reference/visualize/gradient/#definitions-radial)
     * 
     * The [relative placement](https://typst.app/docs/reference/visualize/gradient/#relativeness) of the gradient.
     * 
     * For an element placed at the root/top level of the document, the parent is the page itself. For other elements, the parent is the innermost block, box, column, grid, or stack that contains the element.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"self"` | Relative to itself (its own bounding box). |
     * | `"parent"` | Relative to its parent (the parent's bounding box). |
     * 
     * Typst type: auto|str
     */
    val relative: Smart<PaintRelativeTo> = TAuto,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-radial](https://typst.app/docs/reference/visualize/gradient/#definitions-radial)
     * 
     * The center of the end circle of the gradient.
     * 
     * A value of `(50%, 50%)` means that the end circle is centered inside of its container.
     * 
     * Typst type: array
     */
    val center: TPoint<TRatio> = TPoint(TRatio(0.5.t), TRatio(0.5.t)),
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-radial](https://typst.app/docs/reference/visualize/gradient/#definitions-radial)
     * 
     * The radius of the end circle of the gradient.
     * 
     * By default, it is set to `50%`. The ending radius must be bigger than the focal radius.
     * 
     * Typst type: ratio
     */
    val radius: TRatio = TRatio(0.5.t),
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-radial](https://typst.app/docs/reference/visualize/gradient/#definitions-radial)
     * 
     * The center of the focal circle of the gradient.
     * 
     * The focal center must be inside of the end circle.
     * 
     * A value of `(50%, 50%)` means that the focal circle is centered inside of its container.
     * 
     * By default it is set to the same as the center of the last circle.
     * 
     * Typst type: auto|array
     */
    val focalCenter: Smart<TPoint<TRatio>> = TAuto,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/gradient/#definitions-radial](https://typst.app/docs/reference/visualize/gradient/#definitions-radial)
     * 
     * The radius of the focal circle of the gradient.
     * 
     * The focal center must be inside of the end circle.
     * 
     * By default, it is set to `0%`. The focal radius must be smaller than the ending radius`.
     * 
     * Typst type: ratio
     */
    val focalRadius: TRatio = TRatio(0.0.t)
) : TGradient() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("gradient.radial")
    }
}
