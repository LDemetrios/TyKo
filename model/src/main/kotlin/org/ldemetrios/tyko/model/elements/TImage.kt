package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

sealed interface TImageFormat {
    companion object {
        fun fromValue(value: TValue) = when (value) {
            is TDict<*> -> TImageFormatDict.fromValue(value)
            TAuto -> TAuto
            is TStr -> TImageFormatPreset.fromValue(value)
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@SerialName("str")
enum class TImageFormatEncoding(val value: String) : IntoStr {
    RGB8("rgb8"),
    RGBA8("rgba8"),
    LUMA8("luma8"),
    LUMAA8("lumaa8");

    override fun intoValue(): TStr = TStr(value)

    companion object {
        private val valuesByStr by lazy { entries.associateBy { it.value } }
        fun fromValue(value: TValue) =
            if (value is TStr) valuesByStr[value.value]!! else throw AssertionError("Can't convert from $value")
    }
}
@SerialName("dict")

data class TImageFormatDict(
    val encoding: TImageFormatEncoding,
    val width: TInt,
    val height: TInt,
) : IntoDict<IntoValue>, TImageFormat {
    override fun intoValue(): TDict<IntoValue> = TDict(
        mapOf(
            "encoding" to encoding.intoValue(),
            "width" to width,
            "height" to height,
        )
    )

    companion object {
        fun fromValue(value: TValue) = if (value is TDict<*>) TImageFormatDict(
            encoding = TImageFormatEncoding.fromValue(value["encoding"]!!.intoValue()),
            width = value["width"]!!.intoValue() as TInt,
            height = value["height"]!!.intoValue() as TInt,
        ) else throw AssertionError("Can't convert from $value")
    }
}
@SerialName("str")

enum class TImageFormatPreset : IntoStr, TImageFormat {
    PNG, JPG, GIF, SVG, PDF, WEBP;

    override fun intoValue(): TStr = name.lowercase().t

    companion object {
        private val valuesByStr by lazy { entries.associateBy { it.intoValue() } }
        fun fromValue(value: TValue) =
            if (value is TStr) valuesByStr[value]!! else throw AssertionError("Can't convert from $value")
    }
}

//!https://typst.app/docs/reference/visualize/image/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/visualize/image/](https://typst.app/docs/reference/visualize/image/)
 * 
 * A raster or vector graphic.
 * 
 * You can wrap the image in a [`figure`](https://typst.app/docs/reference/model/figure/) to give it a number and caption.
 * 
 * Like most elements, images are *block-level* by default and thus do not integrate themselves into adjacent paragraphs. To force an image to become inline, put it into a [`box`](https://typst.app/docs/reference/layout/box/).
 * 
 * **_Example_**
 * 
 * ```typ
 * #figure(
 *   image("molecular.jpg", width: 80%),
 *   caption: [
 *     A step in the molecular testing
 *     pipeline of our lab.
 *   ],
 * )
 * ```
 * <img src="https://typst.app/assets/docs/znWnPh4HT5GrpkEcbnfOxAAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("image")
data class TImage(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/image/](https://typst.app/docs/reference/visualize/image/)
     * 
     * A [path](https://typst.app/docs/reference/syntax/#paths) to an image file or raw bytes making up an image in one of the supported [formats](https://typst.app/docs/reference/visualize/image/#parameters-format).
     * 
     * Bytes can be used to specify raw pixel data in a row-major, left-to-right, top-to-bottom format.
     * 
     * Required, positional; Typst type: str|bytes
     */
    @all:Positional val source: DataSource,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/image/](https://typst.app/docs/reference/visualize/image/)
     * 
     * The image's format.
     * 
     * By default, the format is detected automatically. Typically, you thus only need to specify this when providing raw bytes as the [`source`](https://typst.app/docs/reference/visualize/image/#parameters-source) (even then, Typst will try to figure out the format automatically, but that's not always possible).
     * 
     * Supported formats are `"png"`, `"jpg"`, `"gif"`, `"svg"`, `"pdf"`, `"webp"` as well as raw pixel data.
     * 
     * Note that several restrictions apply when using PDF files as images:
     * 
     * - When exporting to PDF, any PDF image file used must have a version equal to or lower than the [export target PDF version](https://typst.app/docs/reference/pdf/#pdf-versions).
     * - PDF files as images are currently not supported when exporting with a specific PDF standard, like PDF/A-3 or PDF/UA-1. In these cases, you can instead use SVGs to embed vector images.
     * - The image file must not be password-protected.
     * - Tags in your PDF image will not be preserved. Instead, you must provide an [alternative description](https://typst.app/docs/reference/visualize/image/#parameters-alt) to make the image accessible.
     * 
     * When providing raw pixel data as the `source`, you must specify a dictionary with the following keys as the `format`:
     * 
     * - `encoding` ([str](https://typst.app/docs/reference/foundations/str/)): The encoding of the pixel data. One of:
     *   - `"rgb8"` (three 8-bit channels: red, green, blue)
     *   - `"rgba8"` (four 8-bit channels: red, green, blue, alpha)
     *   - `"luma8"` (one 8-bit channel)
     *   - `"lumaa8"` (two 8-bit channels: luma and alpha)
     * - `width` ([int](https://typst.app/docs/reference/foundations/int/)): The pixel width of the image.
     * - `height` ([int](https://typst.app/docs/reference/foundations/int/)): The pixel height of the image.
     * 
     * The pixel width multiplied by the height multiplied by the channel count for the specified encoding must then match the `source` data.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"png"` | Raster format for illustrations and transparent graphics. |
     * | `"jpg"` | Lossy raster format suitable for photos. |
     * | `"gif"` | Raster format that is typically used for short animated clips. Typst can load GIFs, but they will become static. |
     * | `"webp"` | Raster format that supports both lossy and lossless compression. |
     * | `"svg"` | The vector graphics format of the web. |
     * | `"pdf"` | High-fidelity document and graphics format, with focus on exact reproduction in print. |
     * 
     * Settable; Typst type: auto|str|dictionary
     */
    @all:Settable val format: TImageFormat? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/image/](https://typst.app/docs/reference/visualize/image/)
     * 
     * The width of the image.
     * 
     * Settable; Typst type: auto|relative
     */
    @all:Settable val width: Smart<TRelative>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/image/](https://typst.app/docs/reference/visualize/image/)
     * 
     * The height of the image.
     * 
     * Settable; Typst type: auto|relative|fraction
     */
    @all:Settable val height: Smart<Spacing>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/image/](https://typst.app/docs/reference/visualize/image/)
     * 
     * An alternative description of the image.
     * 
     * This text is used by Assistive Technology (AT) like screen readers to describe the image to users with visual impairments.
     * 
     * When the image is wrapped in a [`figure`](https://typst.app/docs/reference/model/figure/), use this parameter rather than the [figure's `alt` parameter](https://typst.app/docs/reference/model/figure/#parameters-alt) to describe the image. The only exception to this rule is when the image and the other contents in the figure form a single semantic unit. In this case, use the figure's `alt` parameter to describe the entire composition and do not use this parameter.
     * 
     * You can learn how to write good alternative descriptions in the [Accessibility Guide](https://typst.app/docs/guides/accessibility/#textual-representations).
     * 
     * Settable; Typst type: none|str
     */
    @all:Settable val alt: Option<TStr>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/image/](https://typst.app/docs/reference/visualize/image/)
     * 
     * The page number that should be embedded as an image. This attribute only has an effect for PDF files.
     * 
     * Settable; Typst type: int
     */
    @all:Settable val page: TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/image/](https://typst.app/docs/reference/visualize/image/)
     * 
     * How the image should adjust itself to a given area (the area is defined by the `width` and `height` fields). Note that `fit` doesn't visually change anything if the area's aspect ratio is the same as the image's one.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"cover"` | The image should completely cover the area (preserves aspect ratio by cropping the image only horizontally or vertically). This is the default. |
     * | `"contain"` | The image should be fully contained in the area (preserves aspect ratio; doesn't crop the image; one dimension can be narrower than specified). |
     * | `"stretch"` | The image should be stretched so that it exactly fills the area, even if this means that the image will be distorted (doesn't preserve aspect ratio and doesn't crop the image). |
     * 
     * Settable; Typst type: str
     */
    @all:Settable val fit: TStr? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/image/](https://typst.app/docs/reference/visualize/image/)
     * 
     * A hint to viewers how they should scale the image.
     * 
     * When set to `auto`, the default is left up to the viewer. For PNG export, Typst will default to smooth scaling, like most PDF and SVG viewers.
     * 
     * *Note:* The exact look may differ across PDF viewers.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"smooth"` | Scale with a smoothing algorithm such as bilinear interpolation. |
     * | `"pixelated"` | Scale with nearest neighbor or a similar algorithm to preserve the pixelated look of the image. |
     * 
     * Settable; Typst type: auto|str
     */
    @all:Settable val scaling: Smart<TStr>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/visualize/image/](https://typst.app/docs/reference/visualize/image/)
     * 
     * An ICC profile for the image.
     * 
     * ICC profiles define how to interpret the colors in an image. When set to `auto`, Typst will try to extract an ICC profile from the image.
     * 
     * Settable; Typst type: auto|str|bytes
     */
    @all:Settable val icc: Smart<DataSource>? = null,
    override val label: TLabel? = null
) : TSelectableContent<TImage>() {
    override fun elem(): TLocatableElement<TImage> = ELEM

    companion object {
        val ELEM = TLocatableElement<TImage>("image")
    }
}


@SerialName("set-image")
data class TSetImage(
    override val internals: SetRuleInternals? = null,
    val format: TImageFormat? = null,
    val width: Smart<TRelative>? = null,
    val height: Smart<Spacing>? = null,
    val alt: Option<TStr>? = null,
    val page: TInt? = null,
    val fit: TStr? = null,
    val scaling: Smart<TStr>? = null,
    val icc: Smart<DataSource>? = null
) : TSetRule()
