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
    "ellipse",
    ["ellipse", "content"],
    TEllipseImpl::class,
)
public interface TEllipse : TContent {
    public val body: TContentOrNone?

    public val width: TAutoOrRelative?

    public val height: TAutoOrFractionOrRelative?

    public val fill: TColorOrGradientGradientOrNoneOrTiling?

    public val stroke:
            TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>?

    public val inset: TDictionaryOrRelative<TValue>?

    public val outset: TDictionaryOrRelative<TValue>?

    override fun func(): TElement = TEllipse

    public companion object : TElementImpl("ellipse") {
        internal val bodyType: InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))

        internal val widthType: InternalType = UnionType(ConcreteType("auto"), ConcreteType("relative"))

        internal val heightType: InternalType = UnionType(ConcreteType("auto"),
                ConcreteType("fraction"), ConcreteType("relative"))

        internal val fillType: InternalType = UnionType(ConcreteType("color"), ConcreteType("gradient"),
                ConcreteType("none"), ConcreteType("tiling"))

        internal val strokeType: InternalType = UnionType(ConcreteType("auto"), ConcreteType("color"),
                ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"),
                ConcreteType("length"), ConcreteType("none"), ConcreteType("stroke"),
                ConcreteType("tiling"))

        internal val insetType: InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)),
                ConcreteType("relative"))

        internal val outsetType: InternalType = UnionType(ConcreteType("dictionary", listOf(AnyType)),
                ConcreteType("relative"))
    }
}

internal data class TEllipseImpl(
    @SerialName("body")
    override val body: TContentOrNone? = null,
    @SerialName("width")
    override val width: TAutoOrRelative? = null,
    @SerialName("height")
    override val height: TAutoOrFractionOrRelative? = null,
    @SerialName("fill")
    override val fill: TColorOrGradientGradientOrNoneOrTiling? = null,
    @SerialName("stroke")
    override val stroke:
            TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>? = null,
    @SerialName("inset")
    override val inset: TDictionaryOrRelative<TValue>? = null,
    @SerialName("outset")
    override val outset: TDictionaryOrRelative<TValue>? = null,
    @SerialName("label")
    override val label: TLabel? = null,
) : TEllipse {
    override fun format(): String = Representations.elementRepr("ellipse",ArgumentEntry(false, null,
            `body`),ArgumentEntry(false, "width", `width`),ArgumentEntry(false, "height",
            `height`),ArgumentEntry(false, "fill", `fill`),ArgumentEntry(false, "stroke",
            `stroke`),ArgumentEntry(false, "inset", `inset`),ArgumentEntry(false, "outset",
            `outset`),ArgumentEntry(false, "label", `label`),)
}

@TypstOverloads
public fun TEllipse(
    body: TContentOrNone? = null,
    width: TAutoOrRelative? = null,
    height: TAutoOrFractionOrRelative? = null,
    fill: TColorOrGradientGradientOrNoneOrTiling? = null,
    stroke: TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>? = null,
    inset: TDictionaryOrRelative<TValue>? = null,
    outset: TDictionaryOrRelative<TValue>? = null,
    label: TLabel? = null,
): TEllipse = TEllipseImpl(`body`, `width`, `height`, `fill`, `stroke`, `inset`, `outset`, `label`)

@TSetRuleType(
    "ellipse",
    TSetEllipseImpl::class,
)
public interface TSetEllipse : TSetRule {
    override val elem: String
        get() = "ellipse"

    public val body: TContentOrNone?

    public val width: TAutoOrRelative?

    public val height: TAutoOrFractionOrRelative?

    public val fill: TColorOrGradientGradientOrNoneOrTiling?

    public val stroke:
            TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>?

    public val inset: TDictionaryOrRelative<TValue>?

    public val outset: TDictionaryOrRelative<TValue>?

    override fun format(): String = Representations.setRepr("ellipse",ArgumentEntry(false, null,
            `body`),ArgumentEntry(false, "width", `width`),ArgumentEntry(false, "height",
            `height`),ArgumentEntry(false, "fill", `fill`),ArgumentEntry(false, "stroke",
            `stroke`),ArgumentEntry(false, "inset", `inset`),ArgumentEntry(false, "outset", `outset`),)
}

internal class TSetEllipseImpl(
    @SerialName("body")
    override val body: TContentOrNone? = null,
    @SerialName("width")
    override val width: TAutoOrRelative? = null,
    @SerialName("height")
    override val height: TAutoOrFractionOrRelative? = null,
    @SerialName("fill")
    override val fill: TColorOrGradientGradientOrNoneOrTiling? = null,
    @SerialName("stroke")
    override val stroke:
            TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>? = null,
    @SerialName("inset")
    override val inset: TDictionaryOrRelative<TValue>? = null,
    @SerialName("outset")
    override val outset: TDictionaryOrRelative<TValue>? = null,
) : TSetEllipse

@TypstOverloads
public fun TSetEllipse(
    body: TContentOrNone? = null,
    width: TAutoOrRelative? = null,
    height: TAutoOrFractionOrRelative? = null,
    fill: TColorOrGradientGradientOrNoneOrTiling? = null,
    stroke: TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>? = null,
    inset: TDictionaryOrRelative<TValue>? = null,
    outset: TDictionaryOrRelative<TValue>? = null,
): TSetEllipse = TSetEllipseImpl(`body`, `width`, `height`, `fill`, `stroke`, `inset`, `outset`)
