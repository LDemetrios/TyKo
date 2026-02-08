package org.ldemetrios.tyko.model

import java.awt.Color

import kotlinx.serialization.Serializable


sealed class TColor : TValue, Option<TColor>, ArrayOrSingle<TColor>, TPaint, Smart<TColor>, SidesSplat<TColor> {
    override fun type(): TType = TYPE
    abstract fun func(): TFunc

    companion object {
        val TYPE = TType.COLOR
    }
}


@SerialName("luma")
data class TLumaColor(@all:Positional val lightness: TColorComponent, @all:Positional val alpha: TRatio? = null) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("luma")
    }
}

@SerialName("oklab")
data class TOklabColor(
    @all:Positional val lightness: TRatio,
    @all:Positional val a: TFloat,
    @all:Positional val b: TFloat,
    @all:Positional val alpha: TRatio? = null
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("oklab")
    }
}


@SerialName("oklch")
data class TOklchColor(
    @all:Positional val lightness: TRatio,
    @all:Positional val chroma: TFloat,
    @all:Positional val hue: TAngle,
    @all:Positional val alpha: TRatio? = null
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("oklch")
    }
}


@SerialName("color.linear-rgb")
data class TLinearRgbColor(
    @all:Positional val red: TColorComponent,
    @all:Positional val green: TColorComponent,
    @all:Positional val blue: TColorComponent,
    @all:Positional val alpha: TColorComponent? = null
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("color.linear-rgb")
    }
}


@SerialName("rgb")
data class TRgbColor(
    @all:Positional val red: TColorComponent,
    @all:Positional val green: TColorComponent,
    @all:Positional val blue: TColorComponent,
    @all:Positional val alpha: TColorComponent? = null
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("rgb")
    }
}


@SerialName("color.hsl")
data class THslColor(
    @all:Positional val hue: TAngle,
    @all:Positional val saturation: TColorComponent,
    @all:Positional val lightness: TColorComponent,
    @all:Positional val alpha: TColorComponent? = null
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("color.hsl")
    }
}


@SerialName("color.hsv")
data class THsvColor(
    @all:Positional val hue: TAngle,
    @all:Positional val saturation: TColorComponent,
    @all:Positional val value: TColorComponent,
    @all:Positional val alpha: TColorComponent? = null
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("color.hsv")
    }
}

@SerialName("cmyk")
data class TCmykColor(
    @all:Positional val cyan : TRatio,
    @all:Positional val magenta : TRatio,
    @all:Positional val yellow : TRatio,
    @all:Positional val key : TRatio,
) : TColor() {
    override fun func(): TFunc = FUNC

    companion object {
        val FUNC = TNativeFunc("cmyk")
    }
}

val Color.t get() = TRgbColor(red.t, green.t, blue.t, alpha.t)
