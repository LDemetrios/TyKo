package org.ldemetrios.tyko.model

import java.awt.Color

import org.ldemetrios.tyko.model.TColorComponent
import org.ldemetrios.tyko.model.TPaint

//!https://typst.app/docs/reference/visualize/color/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/color/](https://typst.app/docs/reference/visualize/color/)
 * 
 * A color in a specific color space.
 * 
 * Typst supports:
 * 
 * - sRGB through the [`rgb` function](https://typst.app/docs/reference/visualize/color/#definitions-rgb)
 * - Device CMYK through the [`cmyk` function](https://typst.app/docs/reference/visualize/color/#definitions-cmyk)
 * - D65 Gray through the [`luma` function](https://typst.app/docs/reference/visualize/color/#definitions-luma)
 * - Oklab through the [`oklab` function](https://typst.app/docs/reference/visualize/color/#definitions-oklab)
 * - Oklch through the [`oklch` function](https://typst.app/docs/reference/visualize/color/#definitions-oklch)
 * - Linear RGB through the [`color.linear-rgb` function](https://typst.app/docs/reference/visualize/color/#definitions-linear-rgb)
 * - HSL through the [`color.hsl` function](https://typst.app/docs/reference/visualize/color/#definitions-hsl)
 * - HSV through the [`color.hsv` function](https://typst.app/docs/reference/visualize/color/#definitions-hsv)
 * 
 * **_Example_**
 * 
 * ```typ
 * #rect(fill: aqua)
 * ```
 * <img src="https://typst.app/assets/docs/k-6wh2l9TTXmPhzZxpahjQAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Predefined colors_**
 * 
 * Typst defines the following built-in colors:
 * 
 * | Color | Definition |
 * | --- | --- |
 * | `black` | `luma(0)` |
 * | `gray` | `luma(170)` |
 * | `silver` | `luma(221)` |
 * | `white` | `luma(255)` |
 * | `navy` | `rgb("#001f3f")` |
 * | `blue` | `rgb("#0074d9")` |
 * | `aqua` | `rgb("#7fdbff")` |
 * | `teal` | `rgb("#39cccc")` |
 * | `eastern` | `rgb("#239dad")` |
 * | `purple` | `rgb("#b10dc9")` |
 * | `fuchsia` | `rgb("#f012be")` |
 * | `maroon` | `rgb("#85144b")` |
 * | `red` | `rgb("#ff4136")` |
 * | `orange` | `rgb("#ff851b")` |
 * | `yellow` | `rgb("#ffdc00")` |
 * | `olive` | `rgb("#3d9970")` |
 * | `green` | `rgb("#2ecc40")` |
 * | `lime` | `rgb("#01ff70")` |
 * 
 * The predefined colors and the most important color constructors are available globally and also in the color type's scope, so you can write either `color.red` or just `red`.
 * 
 * <img src="https://typst.app/assets/docs/IWvUAQq21Ue1zu9gwjch-gAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Predefined color maps_**
 * 
 * Typst also includes a number of preset color maps that can be used for [gradients](https://typst.app/docs/reference/visualize/gradient/#stops). These are simply arrays of colors defined in the module `color.map`.
 * 
 * ```typ
 * #circle(fill: gradient.linear(..color.map.crest))
 * ```
 * <img src="https://typst.app/assets/docs/uG6iVgmQwH_6_-1N42yKHwAAAAAAAAAA.png" alt="Preview" />
 * 
 * | Map | Details |
 * | --- | --- |
 * | `turbo` | A perceptually uniform rainbow-like color map. Read [this blog post](https://ai.googleblog.com/2019/08/turbo-improved-rainbow-colormap-for.html) for more details. |
 * | `cividis` | A blue to gray to yellow color map. See [this blog post](https://bids.github.io/colormap/) for more details. |
 * | `rainbow` | Cycles through the full color spectrum. This color map is best used by setting the interpolation color space to [HSL](https://typst.app/docs/reference/visualize/color/#definitions-hsl). The rainbow gradient is __not suitable__ for data visualization because it is not perceptually uniform, so the differences between values become unclear to your readers. It should only be used for decorative purposes. |
 * | `spectral` | Red to yellow to blue color map. |
 * | `viridis` | A purple to teal to yellow color map. |
 * | `inferno` | A black to red to yellow color map. |
 * | `magma` | A black to purple to yellow color map. |
 * | `plasma` | A purple to pink to yellow color map. |
 * | `rocket` | A black to red to white color map. |
 * | `mako` | A black to teal to white color map. |
 * | `vlag` | A light blue to white to red color map. |
 * | `icefire` | A light teal to black to orange color map. |
 * | `flare` | A orange to purple color map that is perceptually uniform. |
 * | `crest` | A light green to blue color map. |
 * 
 * Some popular presets are not included because they are not available under a free licence. Others, like [Jet](https://jakevdp.github.io/blog/2014/10/16/how-bad-is-your-colormap/), are not included because they are not color blind friendly. Feel free to use or create a package with other presets that are useful to you!
 * 
 * <img src="https://typst.app/assets/docs/S2ExoTDRK30Xf9wXJbWIZgAAAAAAAAAA.png" alt="Preview" />
 */
sealed class TColor : TValue, Option<TColor>, ArrayOrSingle<TColor>, TPaint, Smart<TColor>, SidesSplat<TColor> {
    override fun type(): TType = TYPE
    abstract fun func(): TFunc

    companion object {
        val TYPE = TType.COLOR
    }
}

//!https://typst.app/docs/reference/visualize/color/#definitions-luma
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-luma](https://typst.app/docs/reference/visualize/color/#definitions-luma)
 * 
 * Create a grayscale color.
 * 
 * A grayscale color is represented internally by a single `lightness` component.
 * 
 * These components are also available using the [`components`](https://typst.app/docs/reference/visualize/color/#definitions-components) method.
 * 
 * 
 */
@SerialName("luma")
data class TLumaColor(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-luma](https://typst.app/docs/reference/visualize/color/#definitions-luma)
     * 
     * The lightness component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val lightness: TColorComponent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-luma](https://typst.app/docs/reference/visualize/color/#definitions-luma)
     * 
     * The alpha component.
     * 
     * Required, positional; Typst type: ratio
     */
    @all:Positional val alpha: TRatio? = null
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("luma")
    }
}

//!https://typst.app/docs/reference/visualize/color/#definitions-oklab
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-oklab](https://typst.app/docs/reference/visualize/color/#definitions-oklab)
 * 
 * Create an [Oklab](https://bottosson.github.io/posts/oklab/) color.
 * 
 * This color space is well suited for the following use cases:
 * 
 * - Color manipulation such as saturating while keeping perceived hue
 * - Creating grayscale images with uniform perceived lightness
 * - Creating smooth and uniform color transition and gradients
 * 
 * A linear Oklab color is represented internally by an array of four components:
 * 
 * - lightness ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - a ([`float`](https://typst.app/docs/reference/foundations/float/) or [`ratio`](https://typst.app/docs/reference/layout/ratio/). Ratios are relative to `0.4`; meaning `50%` is equal to `0.2`)
 * - b ([`float`](https://typst.app/docs/reference/foundations/float/) or [`ratio`](https://typst.app/docs/reference/layout/ratio/). Ratios are relative to `0.4`; meaning `50%` is equal to `0.2`)
 * - alpha ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * 
 * These components are also available using the [`components`](https://typst.app/docs/reference/visualize/color/#definitions-components) method.
 * 
 * 
 */
@SerialName("oklab")
data class TOklabColor(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-oklab](https://typst.app/docs/reference/visualize/color/#definitions-oklab)
     * 
     * The lightness component.
     * 
     * Required, positional; Typst type: ratio
     */
    @all:Positional val lightness: TRatio,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-oklab](https://typst.app/docs/reference/visualize/color/#definitions-oklab)
     * 
     * The a ("green/red") component.
     * 
     * Required, positional; Typst type: float|ratio
     */
    @all:Positional val a: TFloat,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-oklab](https://typst.app/docs/reference/visualize/color/#definitions-oklab)
     * 
     * The b ("blue/yellow") component.
     * 
     * Required, positional; Typst type: float|ratio
     */
    @all:Positional val b: TFloat,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-oklab](https://typst.app/docs/reference/visualize/color/#definitions-oklab)
     * 
     * The alpha component.
     * 
     * Required, positional; Typst type: ratio
     */
    @all:Positional val alpha: TRatio? = null
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("oklab")
    }
}

//!https://typst.app/docs/reference/visualize/color/#definitions-oklch
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-oklch](https://typst.app/docs/reference/visualize/color/#definitions-oklch)
 * 
 * Create an [Oklch](https://bottosson.github.io/posts/oklab/) color.
 * 
 * This color space is well suited for the following use cases:
 * 
 * - Color manipulation involving lightness, chroma, and hue
 * - Creating grayscale images with uniform perceived lightness
 * - Creating smooth and uniform color transition and gradients
 * 
 * A linear Oklch color is represented internally by an array of four components:
 * 
 * - lightness ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - chroma ([`float`](https://typst.app/docs/reference/foundations/float/) or [`ratio`](https://typst.app/docs/reference/layout/ratio/). Ratios are relative to `0.4`; meaning `50%` is equal to `0.2`)
 * - hue ([`angle`](https://typst.app/docs/reference/layout/angle/))
 * - alpha ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * 
 * These components are also available using the [`components`](https://typst.app/docs/reference/visualize/color/#definitions-components) method.
 * 
 * 
 */
@SerialName("oklch")
data class TOklchColor(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-oklch](https://typst.app/docs/reference/visualize/color/#definitions-oklch)
     * 
     * The lightness component.
     * 
     * Required, positional; Typst type: ratio
     */
    @all:Positional val lightness: TRatio,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-oklch](https://typst.app/docs/reference/visualize/color/#definitions-oklch)
     * 
     * The chroma component.
     * 
     * Required, positional; Typst type: float|ratio
     */
    @all:Positional val chroma: TFloat,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-oklch](https://typst.app/docs/reference/visualize/color/#definitions-oklch)
     * 
     * The hue component.
     * 
     * Required, positional; Typst type: angle
     */
    @all:Positional val hue: TAngle,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-oklch](https://typst.app/docs/reference/visualize/color/#definitions-oklch)
     * 
     * The alpha component.
     * 
     * Required, positional; Typst type: ratio
     */
    @all:Positional val alpha: TRatio? = null
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("oklch")
    }
}

//!https://typst.app/docs/reference/visualize/color/#definitions-linear-rgb
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-linear-rgb](https://typst.app/docs/reference/visualize/color/#definitions-linear-rgb)
 * 
 * Create an RGB(A) color with linear luma.
 * 
 * This color space is similar to sRGB, but with the distinction that the color component are not gamma corrected. This makes it easier to perform color operations such as blending and interpolation. Although, you should prefer to use the [`oklab` function](https://typst.app/docs/reference/visualize/color/#definitions-oklab) for these.
 * 
 * A linear RGB(A) color is represented internally by an array of four components:
 * 
 * - red ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - green ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - blue ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - alpha ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * 
 * These components are also available using the [`components`](https://typst.app/docs/reference/visualize/color/#definitions-components) method.
 * 
 * 
 */
@SerialName("color.linear-rgb")
data class TLinearRgbColor(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-linear-rgb](https://typst.app/docs/reference/visualize/color/#definitions-linear-rgb)
     * 
     * The red component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val red: TColorComponent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-linear-rgb](https://typst.app/docs/reference/visualize/color/#definitions-linear-rgb)
     * 
     * The green component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val green: TColorComponent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-linear-rgb](https://typst.app/docs/reference/visualize/color/#definitions-linear-rgb)
     * 
     * The blue component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val blue: TColorComponent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-linear-rgb](https://typst.app/docs/reference/visualize/color/#definitions-linear-rgb)
     * 
     * The alpha component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val alpha: TColorComponent? = null
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("color.linear-rgb")
    }
}

//!https://typst.app/docs/reference/visualize/color/#definitions-rgb
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-rgb](https://typst.app/docs/reference/visualize/color/#definitions-rgb)
 * 
 * Create an RGB(A) color.
 * 
 * The color is specified in the sRGB color space.
 * 
 * An RGB(A) color is represented internally by an array of four components:
 * 
 * - red ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - green ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - blue ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - alpha ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * 
 * These components are also available using the [`components`](https://typst.app/docs/reference/visualize/color/#definitions-components) method.
 * 
 * 
 */
@SerialName("rgb")
data class TRgbColor(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-rgb](https://typst.app/docs/reference/visualize/color/#definitions-rgb)
     * 
     * The red component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val red: TColorComponent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-rgb](https://typst.app/docs/reference/visualize/color/#definitions-rgb)
     * 
     * The green component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val green: TColorComponent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-rgb](https://typst.app/docs/reference/visualize/color/#definitions-rgb)
     * 
     * The blue component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val blue: TColorComponent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-rgb](https://typst.app/docs/reference/visualize/color/#definitions-rgb)
     * 
     * The alpha component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val alpha: TColorComponent? = null
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("rgb")
    }
}

//!https://typst.app/docs/reference/visualize/color/#definitions-hsl
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-hsl](https://typst.app/docs/reference/visualize/color/#definitions-hsl)
 * 
 * Create an HSL color.
 * 
 * This color space is useful for specifying colors by hue, saturation and lightness. It is also useful for color manipulation, such as saturating while keeping perceived hue.
 * 
 * An HSL color is represented internally by an array of four components:
 * 
 * - hue ([`angle`](https://typst.app/docs/reference/layout/angle/))
 * - saturation ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - lightness ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - alpha ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * 
 * These components are also available using the [`components`](https://typst.app/docs/reference/visualize/color/#definitions-components) method.
 * 
 * 
 */
@SerialName("color.hsl")
data class THslColor(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-hsl](https://typst.app/docs/reference/visualize/color/#definitions-hsl)
     * 
     * The hue angle.
     * 
     * Required, positional; Typst type: angle
     */
    @all:Positional val hue: TAngle,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-hsl](https://typst.app/docs/reference/visualize/color/#definitions-hsl)
     * 
     * The saturation component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val saturation: TColorComponent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-hsl](https://typst.app/docs/reference/visualize/color/#definitions-hsl)
     * 
     * The lightness component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val lightness: TColorComponent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-hsl](https://typst.app/docs/reference/visualize/color/#definitions-hsl)
     * 
     * The alpha component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val alpha: TColorComponent? = null
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("color.hsl")
    }
}

//!https://typst.app/docs/reference/visualize/color/#definitions-hsv
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-hsv](https://typst.app/docs/reference/visualize/color/#definitions-hsv)
 * 
 * Create an HSV color.
 * 
 * This color space is useful for specifying colors by hue, saturation and value. It is also useful for color manipulation, such as saturating while keeping perceived hue.
 * 
 * An HSV color is represented internally by an array of four components:
 * 
 * - hue ([`angle`](https://typst.app/docs/reference/layout/angle/))
 * - saturation ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - value ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - alpha ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * 
 * These components are also available using the [`components`](https://typst.app/docs/reference/visualize/color/#definitions-components) method.
 * 
 * 
 */
@SerialName("color.hsv")
data class THsvColor(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-hsv](https://typst.app/docs/reference/visualize/color/#definitions-hsv)
     * 
     * The hue angle.
     * 
     * Required, positional; Typst type: angle
     */
    @all:Positional val hue: TAngle,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-hsv](https://typst.app/docs/reference/visualize/color/#definitions-hsv)
     * 
     * The saturation component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val saturation: TColorComponent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-hsv](https://typst.app/docs/reference/visualize/color/#definitions-hsv)
     * 
     * The value component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val value: TColorComponent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-hsv](https://typst.app/docs/reference/visualize/color/#definitions-hsv)
     * 
     * The alpha component.
     * 
     * Required, positional; Typst type: int|ratio
     */
    @all:Positional val alpha: TColorComponent? = null
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("color.hsv")
    }
}

//!https://typst.app/docs/reference/visualize/color/#definitions-cmyk
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-cmyk](https://typst.app/docs/reference/visualize/color/#definitions-cmyk)
 * 
 * Create a CMYK color.
 * 
 * This is useful if you want to target a specific printer. The conversion to RGB for display preview might differ from how your printer reproduces the color.
 * 
 * A CMYK color is represented internally by an array of four components:
 * 
 * - cyan ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - magenta ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - yellow ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * - key ([`ratio`](https://typst.app/docs/reference/layout/ratio/))
 * 
 * These components are also available using the [`components`](https://typst.app/docs/reference/visualize/color/#definitions-components) method.
 * 
 * Note that CMYK colors are not currently supported when PDF/A output is enabled.
 * 
 * 
 */
@SerialName("cmyk")
data class TCmykColor(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-cmyk](https://typst.app/docs/reference/visualize/color/#definitions-cmyk)
     * 
     * The cyan component.
     * 
     * Required, positional; Typst type: ratio
     */
    @all:Positional val cyan : TRatio,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-cmyk](https://typst.app/docs/reference/visualize/color/#definitions-cmyk)
     * 
     * The magenta component.
     * 
     * Required, positional; Typst type: ratio
     */
    @all:Positional val magenta : TRatio,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-cmyk](https://typst.app/docs/reference/visualize/color/#definitions-cmyk)
     * 
     * The yellow component.
     * 
     * Required, positional; Typst type: ratio
     */
    @all:Positional val yellow : TRatio,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/color/#definitions-cmyk](https://typst.app/docs/reference/visualize/color/#definitions-cmyk)
     * 
     * The key component.
     * 
     * Required, positional; Typst type: ratio
     */
    @all:Positional val key : TRatio,
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("cmyk")
    }
}

/**
 * Convenience extension converting `Color` into the corresponding Typst value.
 */
val Color.t get() = TRgbColor(red.t, green.t, blue.t, alpha.t)
