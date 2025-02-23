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
    "curve",
    ["curve", "content"],
    TCurveImpl::class,
)
public interface TCurve : TContent {
    public val components: TArray<TContent>

    public val fill: TColorOrGradientGradientOrNoneOrTiling?

    public val fillRule: TFillRule?

    public val stroke:
            TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>?

    override fun func(): TElement = TCurve

    public companion object : TElementImpl("curve") {
        internal val componentsType: InternalType = ConcreteType("array",
                listOf(ConcreteType("content")))

        internal val fillType: InternalType = UnionType(ConcreteType("color"), ConcreteType("gradient"),
                ConcreteType("none"), ConcreteType("tiling"))

        internal val fillRuleType: InternalType = ConcreteType("fill-rule")

        internal val strokeType: InternalType = UnionType(ConcreteType("auto"), ConcreteType("color"),
                ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"),
                ConcreteType("length"), ConcreteType("none"), ConcreteType("stroke"),
                ConcreteType("tiling"))
    }
}

internal data class TCurveImpl(
    @SerialName("components")
    override val components: TArray<TContent>,
    @SerialName("fill")
    override val fill: TColorOrGradientGradientOrNoneOrTiling? = null,
    @SerialName("fill-rule")
    override val fillRule: TFillRule? = null,
    @SerialName("stroke")
    override val stroke:
            TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>? = null,
    @SerialName("label")
    override val label: TLabel? = null,
) : TCurve {
    override fun format(): String = Representations.elementRepr("curve",ArgumentEntry(true, null,
            `components`),ArgumentEntry(false, "fill", `fill`),ArgumentEntry(false, "fill-rule",
            `fillRule`),ArgumentEntry(false, "stroke", `stroke`),ArgumentEntry(false, "label", `label`),)
}

@TypstOverloads
public fun TCurve(
    @TVararg components: TArray<TContent>,
    fill: TColorOrGradientGradientOrNoneOrTiling? = null,
    fillRule: TFillRule? = null,
    stroke: TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>? = null,
    label: TLabel? = null,
): TCurve = TCurveImpl(`components`, `fill`, `fillRule`, `stroke`, `label`)

@TSetRuleType(
    "curve",
    TSetCurveImpl::class,
)
public interface TSetCurve : TSetRule {
    override val elem: String
        get() = "curve"

    public val fill: TColorOrGradientGradientOrNoneOrTiling?

    public val fillRule: TFillRule?

    public val stroke:
            TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>?

    override fun format(): String = Representations.setRepr("curve",ArgumentEntry(false, "fill",
            `fill`),ArgumentEntry(false, "fill-rule", `fillRule`),ArgumentEntry(false, "stroke",
            `stroke`),)
}

internal class TSetCurveImpl(
    @SerialName("fill")
    override val fill: TColorOrGradientGradientOrNoneOrTiling? = null,
    @SerialName("fill-rule")
    override val fillRule: TFillRule? = null,
    @SerialName("stroke")
    override val stroke:
            TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>? = null,
) : TSetCurve

@TypstOverloads
public fun TSetCurve(
    fill: TColorOrGradientGradientOrNoneOrTiling? = null,
    fillRule: TFillRule? = null,
    stroke: TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>? = null,
): TSetCurve = TSetCurveImpl(`fill`, `fillRule`, `stroke`)
