@file:Suppress("unused", "RedundantVisibilityModifier")

package org.ldemetrios.tyko.model

import kotlin.String
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.js.JSBoolean
import org.ldemetrios.js.JSNumber
import org.ldemetrios.js.JSObject
import org.ldemetrios.js.JSString
import org.ldemetrios.js.JSUndefined
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

@TInterfaceType(
    "image",
    ["image", "content"],
    TImageImpl::class,
)
public interface TImage : TContent {
    public val source: TBytesOrStr

    public val format: TAutoOrDictionaryOrImageFormat<TIntOrStr>?

    public val width: TAutoOrRelative?

    public val height: TAutoOrFractionOrRelative?

    public val alt: TNoneOrStr?

    public val fit: TImageFit?

    public val scaling: TAutoOrImageScaling?

    public val icc: TAutoOrBytesOrStr?

    override fun func(): TElement = TImage

    public companion object : TElementImpl("image") {
        internal val sourceType: InternalType = UnionType(ConcreteType("bytes"), ConcreteType("str"))

        internal val formatType: InternalType = UnionType(ConcreteType("auto"),
                ConcreteType("dictionary", listOf(UnionType(ConcreteType("int"), ConcreteType("str")))),
                ConcreteType("image-format"))

        internal val widthType: InternalType = UnionType(ConcreteType("auto"), ConcreteType("relative"))

        internal val heightType: InternalType = UnionType(ConcreteType("auto"),
                ConcreteType("fraction"), ConcreteType("relative"))

        internal val altType: InternalType = UnionType(ConcreteType("none"), ConcreteType("str"))

        internal val fitType: InternalType = ConcreteType("image-fit")

        internal val scalingType: InternalType = UnionType(ConcreteType("auto"),
                ConcreteType("image-scaling"))

        internal val iccType: InternalType = UnionType(ConcreteType("auto"), ConcreteType("bytes"),
                ConcreteType("str"))
    }
}

internal data class TImageImpl(
    @SerialName("source")
    override val source: TBytesOrStr,
    @SerialName("format")
    override val format: TAutoOrDictionaryOrImageFormat<TIntOrStr>? = null,
    @SerialName("width")
    override val width: TAutoOrRelative? = null,
    @SerialName("height")
    override val height: TAutoOrFractionOrRelative? = null,
    @SerialName("alt")
    override val alt: TNoneOrStr? = null,
    @SerialName("fit")
    override val fit: TImageFit? = null,
    @SerialName("scaling")
    override val scaling: TAutoOrImageScaling? = null,
    @SerialName("icc")
    override val icc: TAutoOrBytesOrStr? = null,
    @SerialName("label")
    override val label: TLabel? = null,
) : TImage {
    override fun format(): String = Representations.elementRepr("image",ArgumentEntry(false, null,
            `source`),ArgumentEntry(false, "format", `format`),ArgumentEntry(false, "width",
            `width`),ArgumentEntry(false, "height", `height`),ArgumentEntry(false, "alt",
            `alt`),ArgumentEntry(false, "fit", `fit`),ArgumentEntry(false, "scaling",
            `scaling`),ArgumentEntry(false, "icc", `icc`),ArgumentEntry(false, "label", `label`),)
}

@TypstOverloads
public fun TImage(
    source: TBytesOrStr,
    format: TAutoOrDictionaryOrImageFormat<TIntOrStr>? = null,
    width: TAutoOrRelative? = null,
    height: TAutoOrFractionOrRelative? = null,
    alt: TNoneOrStr? = null,
    fit: TImageFit? = null,
    scaling: TAutoOrImageScaling? = null,
    icc: TAutoOrBytesOrStr? = null,
    label: TLabel? = null,
): TImage = TImageImpl(`source`, `format`, `width`, `height`, `alt`, `fit`, `scaling`, `icc`,
        `label`)

@TSetRuleType(
    "image",
    TSetImageImpl::class,
)
public interface TSetImage : TSetRule {
    override val elem: String
        get() = "image"

    public val format: TAutoOrDictionaryOrImageFormat<TIntOrStr>?

    public val width: TAutoOrRelative?

    public val height: TAutoOrFractionOrRelative?

    public val alt: TNoneOrStr?

    public val fit: TImageFit?

    public val scaling: TAutoOrImageScaling?

    public val icc: TAutoOrBytesOrStr?

    override fun format(): String = Representations.setRepr("image",ArgumentEntry(false, "format",
            `format`),ArgumentEntry(false, "width", `width`),ArgumentEntry(false, "height",
            `height`),ArgumentEntry(false, "alt", `alt`),ArgumentEntry(false, "fit",
            `fit`),ArgumentEntry(false, "scaling", `scaling`),ArgumentEntry(false, "icc", `icc`),)
}

internal class TSetImageImpl(
    @SerialName("format")
    override val format: TAutoOrDictionaryOrImageFormat<TIntOrStr>? = null,
    @SerialName("width")
    override val width: TAutoOrRelative? = null,
    @SerialName("height")
    override val height: TAutoOrFractionOrRelative? = null,
    @SerialName("alt")
    override val alt: TNoneOrStr? = null,
    @SerialName("fit")
    override val fit: TImageFit? = null,
    @SerialName("scaling")
    override val scaling: TAutoOrImageScaling? = null,
    @SerialName("icc")
    override val icc: TAutoOrBytesOrStr? = null,
) : TSetImage

@TypstOverloads
public fun TSetImage(
    format: TAutoOrDictionaryOrImageFormat<TIntOrStr>? = null,
    width: TAutoOrRelative? = null,
    height: TAutoOrFractionOrRelative? = null,
    alt: TNoneOrStr? = null,
    fit: TImageFit? = null,
    scaling: TAutoOrImageScaling? = null,
    icc: TAutoOrBytesOrStr? = null,
): TSetImage = TSetImageImpl(`format`, `width`, `height`, `alt`, `fit`, `scaling`, `icc`)
