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
    "length",
    ["length", "relative"],
    TLengthImpl::class,
)
public interface TLength : TAutoOrLength, TDictionaryOrLength<TDynamic>, TBoolOrLength,
        TArrayOrColorOrDictionaryOrFunctionOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TDynamic, TDynamic>,
        TColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TDynamic>,
        TLengthOrTextTopEdge, TLengthOrTextBottomEdge,
        TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrStrokeOrTiling<TDynamic>,
        TColorOrDictionaryOrGradientGradientOrLengthOrStrokeOrTiling<TDynamic>, TAutoOrLengthOrRatio,
        TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TDynamic>,
        TIntOrLength, TRelative {
    public val pt: TFloat?

    public val em: TFloat?

    override fun type(): TType = TLength

    public companion object : TTypeImpl("length") {
        internal val ptType: InternalType = ConcreteType("float")

        internal val emType: InternalType = ConcreteType("float")
    }
}

internal data class TLengthImpl(
    @SerialName("pt")
    override val pt: TFloat? = null,
    @SerialName("em")
    override val em: TFloat? = null,
) : TLength {
    override fun format(): String = Representations.reprOf(this)
}

@TypstOverloads
public fun TLength(pt: TFloat? = null, em: TFloat? = null): TLength = TLengthImpl(`pt`, `em`)
