package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TText(
	val body: TContent? = null,
	val text: TStr? = null,
	val font: TArrayOrStr<TStr, >? = null,
	val fallback: TBool? = null,
	val style: TTextStyle? = null,
	val weight: TIntOrTextWeight? = null,
	val stretch: TRatio? = null,
	val size: TLength? = null,
	val fill: TColorOrGradientOrPattern? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val tracking: TLength? = null,
	val spacing: TRelative? = null,
	val cjkLatinSpacing: TAutoOrNone? = null,
	val baseline: TLength? = null,
	val overhang: TBool? = null,
	val topEdge: TLengthOrTextTopEdge? = null,
	val bottomEdge: TLengthOrTextBottomEdge? = null,
	val lang: TStr? = null,
	val region: TNoneOrStr? = null,
	val script: TAutoOrStr? = null,
	val dir: TAutoOrDirection? = null,
	val hyphenate: TAutoOrBool? = null,
	val costs: TDictionary<*, >? = null,
	val kerning: TBool? = null,
	val alternates: TBool? = null,
	val stylisticSet: TArrayOrIntOrNone<TInt, >? = null,
	val ligatures: TBool? = null,
	val discretionaryLigatures: TBool? = null,
	val historicalLigatures: TBool? = null,
	val numberType: TAutoOrTextNumberType? = null,
	val numberWidth: TAutoOrTextNumberWidth? = null,
	val slashedZero: TBool? = null,
	val fractions: TBool? = null,
	val features: TArrayOrDictionary<TStr, TInt, >? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"text",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, null, text),
		ArgumentEntry(false, "font", font),
		ArgumentEntry(false, "fallback", fallback),
		ArgumentEntry(false, "style", style),
		ArgumentEntry(false, "weight", weight),
		ArgumentEntry(false, "stretch", stretch),
		ArgumentEntry(false, "size", size),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "tracking", tracking),
		ArgumentEntry(false, "spacing", spacing),
		ArgumentEntry(false, "cjk-latin-spacing", cjkLatinSpacing),
		ArgumentEntry(false, "baseline", baseline),
		ArgumentEntry(false, "overhang", overhang),
		ArgumentEntry(false, "top-edge", topEdge),
		ArgumentEntry(false, "bottom-edge", bottomEdge),
		ArgumentEntry(false, "lang", lang),
		ArgumentEntry(false, "region", region),
		ArgumentEntry(false, "script", script),
		ArgumentEntry(false, "dir", dir),
		ArgumentEntry(false, "hyphenate", hyphenate),
		ArgumentEntry(false, "costs", costs),
		ArgumentEntry(false, "kerning", kerning),
		ArgumentEntry(false, "alternates", alternates),
		ArgumentEntry(false, "stylistic-set", stylisticSet),
		ArgumentEntry(false, "ligatures", ligatures),
		ArgumentEntry(false, "discretionary-ligatures", discretionaryLigatures),
		ArgumentEntry(false, "historical-ligatures", historicalLigatures),
		ArgumentEntry(false, "number-type", numberType),
		ArgumentEntry(false, "number-width", numberWidth),
		ArgumentEntry(false, "slashed-zero", slashedZero),
		ArgumentEntry(false, "fractions", fractions),
		ArgumentEntry(false, "features", features),
	)
	override fun func() = TText
    companion object : TElement("text") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val textType : InternalType = ConcreteType("str")
        internal val fontType : InternalType = UnionType(ConcreteType("array", listOf(ConcreteType("str"))), ConcreteType("str"))
        internal val fallbackType : InternalType = ConcreteType("bool")
        internal val styleType : InternalType = ConcreteType("text-style")
        internal val weightType : InternalType = UnionType(ConcreteType("int"), ConcreteType("text-weight"))
        internal val stretchType : InternalType = ConcreteType("ratio")
        internal val sizeType : InternalType = ConcreteType("length")
        internal val fillType : InternalType = UnionType(ConcreteType("color"), ConcreteType("gradient"), ConcreteType("pattern"))
        internal val strokeType : InternalType = UnionType(ConcreteType("color"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("gradient"), ConcreteType("length"), ConcreteType("none"), ConcreteType("pattern"), ConcreteType("stroke"))
        internal val trackingType : InternalType = ConcreteType("length")
        internal val spacingType : InternalType = ConcreteType("relative")
        internal val cjkLatinSpacingType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("none"))
        internal val baselineType : InternalType = ConcreteType("length")
        internal val overhangType : InternalType = ConcreteType("bool")
        internal val topEdgeType : InternalType = UnionType(ConcreteType("length"), ConcreteType("text-top-edge"))
        internal val bottomEdgeType : InternalType = UnionType(ConcreteType("length"), ConcreteType("text-bottom-edge"))
        internal val langType : InternalType = ConcreteType("str")
        internal val regionType : InternalType = UnionType(ConcreteType("none"), ConcreteType("str"))
        internal val scriptType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("str"))
        internal val dirType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("direction"))
        internal val hyphenateType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("bool"))
        internal val costsType : InternalType = ConcreteType("dictionary", listOf(AnyType))
        internal val kerningType : InternalType = ConcreteType("bool")
        internal val alternatesType : InternalType = ConcreteType("bool")
        internal val stylisticSetType : InternalType = UnionType(ConcreteType("array", listOf(ConcreteType("int"))), ConcreteType("int"), ConcreteType("none"))
        internal val ligaturesType : InternalType = ConcreteType("bool")
        internal val discretionaryLigaturesType : InternalType = ConcreteType("bool")
        internal val historicalLigaturesType : InternalType = ConcreteType("bool")
        internal val numberTypeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("text-number-type"))
        internal val numberWidthType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("text-number-width"))
        internal val slashedZeroType : InternalType = ConcreteType("bool")
        internal val fractionsType : InternalType = ConcreteType("bool")
        internal val featuresType : InternalType = UnionType(ConcreteType("array", listOf(ConcreteType("str"))), ConcreteType("dictionary", listOf(ConcreteType("int"))))

    }
}

data class TSetText(
	val font: TArrayOrStr<TStr, >? = null,
	val fallback: TBool? = null,
	val style: TTextStyle? = null,
	val weight: TIntOrTextWeight? = null,
	val stretch: TRatio? = null,
	val size: TLength? = null,
	val fill: TColorOrGradientOrPattern? = null,
	val stroke: TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null,
	val tracking: TLength? = null,
	val spacing: TRelative? = null,
	val cjkLatinSpacing: TAutoOrNone? = null,
	val baseline: TLength? = null,
	val overhang: TBool? = null,
	val topEdge: TLengthOrTextTopEdge? = null,
	val bottomEdge: TLengthOrTextBottomEdge? = null,
	val lang: TStr? = null,
	val region: TNoneOrStr? = null,
	val script: TAutoOrStr? = null,
	val dir: TAutoOrDirection? = null,
	val hyphenate: TAutoOrBool? = null,
	val costs: TDictionary<*, >? = null,
	val kerning: TBool? = null,
	val alternates: TBool? = null,
	val stylisticSet: TArrayOrIntOrNone<TInt, >? = null,
	val ligatures: TBool? = null,
	val discretionaryLigatures: TBool? = null,
	val historicalLigatures: TBool? = null,
	val numberType: TAutoOrTextNumberType? = null,
	val numberWidth: TAutoOrTextNumberWidth? = null,
	val slashedZero: TBool? = null,
	val fractions: TBool? = null,
	val features: TArrayOrDictionary<TStr, TInt, >? = null,
) : TSetRule("text") {
    override fun format() = Representations.setRepr(
		"text",
		ArgumentEntry(false, "font", font),
		ArgumentEntry(false, "fallback", fallback),
		ArgumentEntry(false, "style", style),
		ArgumentEntry(false, "weight", weight),
		ArgumentEntry(false, "stretch", stretch),
		ArgumentEntry(false, "size", size),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "stroke", stroke),
		ArgumentEntry(false, "tracking", tracking),
		ArgumentEntry(false, "spacing", spacing),
		ArgumentEntry(false, "cjk-latin-spacing", cjkLatinSpacing),
		ArgumentEntry(false, "baseline", baseline),
		ArgumentEntry(false, "overhang", overhang),
		ArgumentEntry(false, "top-edge", topEdge),
		ArgumentEntry(false, "bottom-edge", bottomEdge),
		ArgumentEntry(false, "lang", lang),
		ArgumentEntry(false, "region", region),
		ArgumentEntry(false, "script", script),
		ArgumentEntry(false, "dir", dir),
		ArgumentEntry(false, "hyphenate", hyphenate),
		ArgumentEntry(false, "costs", costs),
		ArgumentEntry(false, "kerning", kerning),
		ArgumentEntry(false, "alternates", alternates),
		ArgumentEntry(false, "stylistic-set", stylisticSet),
		ArgumentEntry(false, "ligatures", ligatures),
		ArgumentEntry(false, "discretionary-ligatures", discretionaryLigatures),
		ArgumentEntry(false, "historical-ligatures", historicalLigatures),
		ArgumentEntry(false, "number-type", numberType),
		ArgumentEntry(false, "number-width", numberWidth),
		ArgumentEntry(false, "slashed-zero", slashedZero),
		ArgumentEntry(false, "fractions", fractions),
		ArgumentEntry(false, "features", features),
	)
}

