package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TStroke(
	val paint: TAutoOrColorOrGradientOrPattern? = null,
	val thickness: TAutoOrLength? = null,
	val cap: TAutoOrStr? = null,
	val join: TAutoOrStr? = null,
	val dash: TArrayOrAutoOrDictionaryOrNoneOrStr<*, *, >? = null,
	val miterLimit: TAutoOrFloat? = null,
) : TValue, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, Nothing>, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing> {
    override fun format() = Representations.structRepr(
		"stroke",
		ArgumentEntry(false, "paint", paint),
		ArgumentEntry(false, "thickness", thickness),
		ArgumentEntry(false, "cap", cap),
		ArgumentEntry(false, "join", join),
		ArgumentEntry(false, "dash", dash),
		ArgumentEntry(false, "miter-limit", miterLimit),
	)
	override fun type(): TType = TStroke
    companion object : TType("stroke") {
        internal val paintType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("color"), ConcreteType("gradient"), ConcreteType("pattern"))
        internal val thicknessType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("length"))
        internal val capType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("str"))
        internal val joinType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("str"))
        internal val dashType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("auto"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("none"), ConcreteType("str"))
        internal val miterLimitType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("float"))

    }
}
