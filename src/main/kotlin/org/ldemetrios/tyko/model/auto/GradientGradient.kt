@file:Suppress("unused", "RedundantVisibilityModifier")

package org.ldemetrios.tyko.model

import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.js.JSBoolean
import org.ldemetrios.js.JSNumber
import org.ldemetrios.js.JSObject
import org.ldemetrios.js.JSString
import org.ldemetrios.js.JSUndefined
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

public interface TGradientGradient :
        TArrayOrColorOrFunctionOrGradientGradientOrNoneOrTiling<TDynamic>,
        TArrayOrColorOrDictionaryOrFunctionOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TDynamic, TDynamic>,
        TAutoOrColorOrGradientGradientOrNoneOrTiling,
        TColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TDynamic>,
        TColorOrGradientGradientOrNoneOrTiling,
        TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrStrokeOrTiling<TDynamic>,
        TColorOrGradientGradientOrTiling,
        TColorOrDictionaryOrGradientGradientOrLengthOrStrokeOrTiling<TDynamic>,
        TAutoOrColorOrDictionaryOrGradientGradientOrLengthOrNoneOrStrokeOrTiling<TDynamic>,
        TAutoOrColorOrGradientGradientOrTiling, TValue {
    override fun type(): TType = TGradientGradient

    public fun func(): TFunction

    public companion object : TTypeImpl("gradient")
}
