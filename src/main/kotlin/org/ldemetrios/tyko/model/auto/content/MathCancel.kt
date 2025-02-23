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
    "math.cancel",
    ["math.cancel", "content"],
    TMathCancelImpl::class,
)
public interface TMathCancel : TContent {
    public val body: TContent

    public val length: TRelative?

    public val inverted: TBool?

    public val cross: TBool?

    public val angle: TAngleOrAutoOrFunction?

    public val stroke: TColorOrDictionaryOrGradientGradientOrLengthOrStrokeOrTiling<TValue>?

    override fun func(): TElement = TMathCancel

    public companion object : TElementImpl("math.cancel") {
        internal val bodyType: InternalType = ConcreteType("content")

        internal val lengthType: InternalType = ConcreteType("relative")

        internal val invertedType: InternalType = ConcreteType("bool")

        internal val crossType: InternalType = ConcreteType("bool")

        internal val angleType: InternalType = UnionType(ConcreteType("angle"), ConcreteType("auto"),
                ConcreteType("function"))

        internal val strokeType: InternalType = UnionType(ConcreteType("color"),
                ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"),
                ConcreteType("length"), ConcreteType("stroke"), ConcreteType("tiling"))
    }
}

internal data class TMathCancelImpl(
    @SerialName("body")
    override val body: TContent,
    @SerialName("length")
    override val length: TRelative? = null,
    @SerialName("inverted")
    override val inverted: TBool? = null,
    @SerialName("cross")
    override val cross: TBool? = null,
    @SerialName("angle")
    override val angle: TAngleOrAutoOrFunction? = null,
    @SerialName("stroke")
    override val stroke: TColorOrDictionaryOrGradientGradientOrLengthOrStrokeOrTiling<TValue>? = null,
    @SerialName("label")
    override val label: TLabel? = null,
) : TMathCancel {
    override fun format(): String = Representations.elementRepr("math.cancel",ArgumentEntry(false,
            null, `body`),ArgumentEntry(false, "length", `length`),ArgumentEntry(false, "inverted",
            `inverted`),ArgumentEntry(false, "cross", `cross`),ArgumentEntry(false, "angle",
            `angle`),ArgumentEntry(false, "stroke", `stroke`),ArgumentEntry(false, "label", `label`),)
}

@TypstOverloads
public fun TMathCancel(
    @TContentBody body: TContent,
    length: TRelative? = null,
    inverted: TBool? = null,
    cross: TBool? = null,
    angle: TAngleOrAutoOrFunction? = null,
    stroke: TColorOrDictionaryOrGradientGradientOrLengthOrStrokeOrTiling<TValue>? = null,
    label: TLabel? = null,
): TMathCancel = TMathCancelImpl(`body`, `length`, `inverted`, `cross`, `angle`, `stroke`, `label`)

@TSetRuleType(
    "math.cancel",
    TSetMathCancelImpl::class,
)
public interface TSetMathCancel : TSetRule {
    override val elem: String
        get() = "math.cancel"

    public val length: TRelative?

    public val inverted: TBool?

    public val cross: TBool?

    public val angle: TAngleOrAutoOrFunction?

    public val stroke: TColorOrDictionaryOrGradientGradientOrLengthOrStrokeOrTiling<TValue>?

    override fun format(): String = Representations.setRepr("math.cancel",ArgumentEntry(false,
            "length", `length`),ArgumentEntry(false, "inverted", `inverted`),ArgumentEntry(false, "cross",
            `cross`),ArgumentEntry(false, "angle", `angle`),ArgumentEntry(false, "stroke", `stroke`),)
}

internal class TSetMathCancelImpl(
    @SerialName("length")
    override val length: TRelative? = null,
    @SerialName("inverted")
    override val inverted: TBool? = null,
    @SerialName("cross")
    override val cross: TBool? = null,
    @SerialName("angle")
    override val angle: TAngleOrAutoOrFunction? = null,
    @SerialName("stroke")
    override val stroke: TColorOrDictionaryOrGradientGradientOrLengthOrStrokeOrTiling<TValue>? = null,
) : TSetMathCancel

@TypstOverloads
public fun TSetMathCancel(
    length: TRelative? = null,
    inverted: TBool? = null,
    cross: TBool? = null,
    angle: TAngleOrAutoOrFunction? = null,
    stroke: TColorOrDictionaryOrGradientGradientOrLengthOrStrokeOrTiling<TValue>? = null,
): TSetMathCancel = TSetMathCancelImpl(`length`, `inverted`, `cross`, `angle`, `stroke`)
