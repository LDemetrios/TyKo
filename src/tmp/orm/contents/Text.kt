package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("text")
@Serializable
data class TText(
    @SerialName("body") val body : TContent? = null, 
    @SerialName("text") val text : TStr? = null, 
    @SerialName("font") val font : TArrayOrStr<TStr, >? = null, 
    @SerialName("fallback") val fallback : TBool? = null, 
    @SerialName("style") val style : TStr? = null, 
    @SerialName("weight") val weight : TIntOrStr? = null, 
    @SerialName("stretch") val stretch : TRatio? = null, 
    @SerialName("size") val size : TLength? = null, 
    @SerialName("fill") val fill : TColorOrGradientOrPattern? = null, 
    @SerialName("stroke") val stroke : TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<*, >? = null, 
    @SerialName("tracking") val tracking : TLength? = null, 
    @SerialName("spacing") val spacing : TRelative? = null, 
    @SerialName("cjk-latin-spacing") val cjkLatinSpacing : TAutoOrNone? = null, 
    @SerialName("baseline") val baseline : TLength? = null, 
    @SerialName("overhang") val overhang : TBool? = null, 
    @SerialName("top-edge") val topEdge : TLengthOrStr? = null, 
    @SerialName("bottom-edge") val bottomEdge : TLengthOrStr? = null, 
    @SerialName("lang") val lang : TStr? = null, 
    @SerialName("region") val region : TNoneOrStr? = null, 
    @SerialName("script") val script : TAutoOrStr? = null, 
    @SerialName("dir") val dir : TAutoOrDirection? = null, 
    @SerialName("hyphenate") val hyphenate : TAutoOrBool? = null, 
    @SerialName("kerning") val kerning : TBool? = null, 
    @SerialName("alternates") val alternates : TBool? = null, 
    @SerialName("stylistic-set") val stylisticSet : TArrayOrIntOrNone<TInt, >? = null, 
    @SerialName("ligatures") val ligatures : TBool? = null, 
    @SerialName("discretionary-ligatures") val discretionaryLigatures : TBool? = null, 
    @SerialName("historical-ligatures") val historicalLigatures : TBool? = null, 
    @SerialName("number-type") val numberType : TAutoOrStr? = null, 
    @SerialName("number-width") val numberWidth : TAutoOrStr? = null, 
    @SerialName("slashed-zero") val slashedZero : TBool? = null, 
    @SerialName("fractions") val fractions : TBool? = null, 
    @SerialName("features") val features : TArrayOrDictionary<TStr, TInt, >? = null, 
    @SerialName("costs") val costs : TDictionary<TRatio, >? = null, 
) : TContent
{
    override fun repr() : String = RT.structRepr("text", ArgumentEntry(false, null, body), ArgumentEntry(false, null, text), ArgumentEntry(false, "font", font), ArgumentEntry(false, "fallback", fallback), ArgumentEntry(false, "style", style), ArgumentEntry(false, "weight", weight), ArgumentEntry(false, "stretch", stretch), ArgumentEntry(false, "size", size), ArgumentEntry(false, "fill", fill), ArgumentEntry(false, "stroke", stroke), ArgumentEntry(false, "tracking", tracking), ArgumentEntry(false, "spacing", spacing), ArgumentEntry(false, "cjk-latin-spacing", cjkLatinSpacing), ArgumentEntry(false, "baseline", baseline), ArgumentEntry(false, "overhang", overhang), ArgumentEntry(false, "top-edge", topEdge), ArgumentEntry(false, "bottom-edge", bottomEdge), ArgumentEntry(false, "lang", lang), ArgumentEntry(false, "region", region), ArgumentEntry(false, "script", script), ArgumentEntry(false, "dir", dir), ArgumentEntry(false, "hyphenate", hyphenate), ArgumentEntry(false, "kerning", kerning), ArgumentEntry(false, "alternates", alternates), ArgumentEntry(false, "stylistic-set", stylisticSet), ArgumentEntry(false, "ligatures", ligatures), ArgumentEntry(false, "discretionary-ligatures", discretionaryLigatures), ArgumentEntry(false, "historical-ligatures", historicalLigatures), ArgumentEntry(false, "number-type", numberType), ArgumentEntry(false, "number-width", numberWidth), ArgumentEntry(false, "slashed-zero", slashedZero), ArgumentEntry(false, "fractions", fractions), ArgumentEntry(false, "features", features), ArgumentEntry(false, "costs", costs), )
}
