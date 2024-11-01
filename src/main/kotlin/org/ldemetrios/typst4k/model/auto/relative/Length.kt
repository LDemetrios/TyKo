package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TLength(
	val pt: TFloat? = null,
	val em: TFloat? = null,
) : TRelative, 
    TAutoOrLength, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, Nothing>, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    THighlightTopEdgeOrLength, 
    THighlightBottomEdgeOrLength, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TLengthOrTextTopEdge, 
    TLengthOrTextBottomEdge, 
    TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TAutoOrLengthOrRatio, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TIntOrLength {
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TLength
    companion object : TType("length") {
        internal val ptType : InternalType = ConcreteType("float")
        internal val emType : InternalType = ConcreteType("float")

    }
}
