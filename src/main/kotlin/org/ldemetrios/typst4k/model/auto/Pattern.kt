package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TPattern(
	val body: TContent? = null,
	val size: TArrayOrAuto<*, >? = null,
	val spacing: TArray<TLength, >? = null,
	val relative: TAutoOrStr? = null,
) : TValue, 
    TArrayOrColorOrFunctionOrGradientOrNoneOrPattern<Nothing>, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, Nothing>, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TColorOrGradientOrNoneOrPattern, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TColorOrGradientOrPattern, 
    TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrGradientOrPattern {
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TPattern
    companion object : TType("pattern") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val sizeType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("auto"))
        internal val spacingType : InternalType = ConcreteType("array", listOf(ConcreteType("length")))
        internal val relativeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("str"))

    }
}
