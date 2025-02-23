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
    "path",
    ["path", "content"],
    TPathImpl::class,
)
public interface TPath : TContent {
    public val vertices: TArray<TArray<TValue>>

    public val fill: TColorOrGradientGradientOrNoneOrTiling?

    public val fillRule: TFillRule?

    public val stroke:
            TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>?

    public val closed: TBool?

    override fun func(): TElement = TPath

    public companion object : TElementImpl("path") {
        internal val verticesType: InternalType = ConcreteType("array", listOf(ConcreteType("array",
                listOf(AnyType))))

        internal val fillType: InternalType = UnionType(ConcreteType("color"), ConcreteType("gradient"),
                ConcreteType("none"), ConcreteType("tiling"))

        internal val fillRuleType: InternalType = ConcreteType("fill-rule")

        internal val strokeType: InternalType = UnionType(ConcreteType("auto"), ConcreteType("color"),
                ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"),
                ConcreteType("length"), ConcreteType("none"), ConcreteType("stroke"),
                ConcreteType("tiling"))

        internal val closedType: InternalType = ConcreteType("bool")
    }
}

internal data class TPathImpl(
    @SerialName("vertices")
    override val vertices: TArray<TArray<TValue>>,
    @SerialName("fill")
    override val fill: TColorOrGradientGradientOrNoneOrTiling? = null,
    @SerialName("fill-rule")
    override val fillRule: TFillRule? = null,
    @SerialName("stroke")
    override val stroke:
            TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>? = null,
    @SerialName("closed")
    override val closed: TBool? = null,
    @SerialName("label")
    override val label: TLabel? = null,
) : TPath {
    override fun format(): String = Representations.elementRepr("path",ArgumentEntry(true, null,
            `vertices`),ArgumentEntry(false, "fill", `fill`),ArgumentEntry(false, "fill-rule",
            `fillRule`),ArgumentEntry(false, "stroke", `stroke`),ArgumentEntry(false, "closed",
            `closed`),ArgumentEntry(false, "label", `label`),)
}

@TypstOverloads
public fun TPath(
    @TVararg vertices: TArray<TArray<TValue>>,
    fill: TColorOrGradientGradientOrNoneOrTiling? = null,
    fillRule: TFillRule? = null,
    stroke: TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>? = null,
    closed: TBool? = null,
    label: TLabel? = null,
): TPath = TPathImpl(`vertices`, `fill`, `fillRule`, `stroke`, `closed`, `label`)

@TSetRuleType(
    "path",
    TSetPathImpl::class,
)
public interface TSetPath : TSetRule {
    override val elem: String
        get() = "path"

    public val fill: TColorOrGradientGradientOrNoneOrTiling?

    public val fillRule: TFillRule?

    public val stroke:
            TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>?

    public val closed: TBool?

    override fun format(): String = Representations.setRepr("path",ArgumentEntry(false, "fill",
            `fill`),ArgumentEntry(false, "fill-rule", `fillRule`),ArgumentEntry(false, "stroke",
            `stroke`),ArgumentEntry(false, "closed", `closed`),)
}

internal class TSetPathImpl(
    @SerialName("fill")
    override val fill: TColorOrGradientGradientOrNoneOrTiling? = null,
    @SerialName("fill-rule")
    override val fillRule: TFillRule? = null,
    @SerialName("stroke")
    override val stroke:
            TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>? = null,
    @SerialName("closed")
    override val closed: TBool? = null,
) : TSetPath

@TypstOverloads
public fun TSetPath(
    fill: TColorOrGradientGradientOrNoneOrTiling? = null,
    fillRule: TFillRule? = null,
    stroke: TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TValue>? = null,
    closed: TBool? = null,
): TSetPath = TSetPathImpl(`fill`, `fillRule`, `stroke`, `closed`)
