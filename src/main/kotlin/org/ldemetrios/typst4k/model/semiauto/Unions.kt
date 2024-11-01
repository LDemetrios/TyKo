package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

@UnionSyntheticType(options = ["array", "str"])
sealed interface TArrayOrStr<out E : TValue> : TValue, 
    TArrayOrAutoOrDictionaryOrStr<E, Nothing>, 
    TArrayOrNoneOrStr<E>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, Nothing>;

@UnionSyntheticType(options = ["auto", "content", "none"])
sealed interface TAutoOrContentOrNone : TValue, 
    TAutoOrContentOrFunctionOrNone;

@UnionSyntheticType(options = ["array", "content", "function"])
sealed interface TArrayOrContentOrFunction<out E : TValue> : TValue;

@UnionSyntheticType(options = ["auto", "length"])
sealed interface TAutoOrLength : TValue, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TAutoOrLengthOrRatio, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>;

@UnionSyntheticType(options = ["content", "none"])
sealed interface TContentOrNone : TValue, 
    TAutoOrContentOrNone, 
    TAutoOrContentOrFunctionOrNone, 
    TContentOrLabelOrNone;

@UnionSyntheticType(options = ["cite-form", "none"])
sealed interface TCiteFormOrNone : TValue;

@UnionSyntheticType(options = ["auto", "cite-style"])
sealed interface TAutoOrCiteStyle : TValue;

@UnionSyntheticType(options = ["auto", "datetime", "none"])
sealed interface TAutoOrDatetimeOrNone : TValue;

@UnionSyntheticType(options = ["alignment", "auto", "none"])
sealed interface TAlignmentOrAutoOrNone : TValue;

@UnionSyntheticType(options = ["auto", "function", "str"])
sealed interface TAutoOrFunctionOrStr : TValue;

@UnionSyntheticType(options = ["auto", "content", "function", "none"])
sealed interface TAutoOrContentOrFunctionOrNone : TValue;

@UnionSyntheticType(options = ["function", "none", "str"])
sealed interface TFunctionOrNoneOrStr : TValue;

@UnionSyntheticType(options = ["auto", "content"])
sealed interface TAutoOrContent : TValue, 
    TAutoOrContentOrNone, 
    TAutoOrContentOrFunctionOrNone;

@UnionSyntheticType(options = ["content", "label"])
sealed interface TContentOrLabel : TValue, 
    TContentOrLabelOrNone;

@UnionSyntheticType(options = ["function", "str"])
sealed interface TFunctionOrStr : TValue, 
    TAutoOrFunctionOrStr, 
    TFunctionOrNoneOrStr, 
    TFunctionOrLabelOrLocationOrSelectorOrStr;

@UnionSyntheticType(options = ["auto", "int"])
sealed interface TAutoOrInt : TValue, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TAutoOrFractionOrIntOrRelative;

@UnionSyntheticType(options = ["auto", "bool"])
sealed interface TAutoOrBool : TValue, 
    TAutoOrBoolOrFunctionOrNoneOrRelative;

@UnionSyntheticType(options = ["label", "location", "position", "str"])
sealed interface TLabelOrLocationOrPositionOrStr : TValue;

@UnionSyntheticType(options = ["array", "enum.item"])
sealed interface TArrayOrEnumItem<out E : TValue> : TValue;

@UnionSyntheticType(options = ["int", "none"])
sealed interface TIntOrNone : TValue, 
    TArrayOrIntOrNone<Nothing>, 
    TDictionaryOrIntOrNone<Nothing>;

@UnionSyntheticType(options = ["function", "label", "location", "selector"])
sealed interface TFunctionOrLabelOrLocationOrSelector : TValue, 
    TFunctionOrLabelOrLocationOrSelectorOrStr;

@UnionSyntheticType(options = ["auto", "bool", "function", "none", "relative"])
sealed interface TAutoOrBoolOrFunctionOrNoneOrRelative : TValue;

@UnionSyntheticType(options = ["auto", "par-linebreaks"])
sealed interface TAutoOrParLinebreaks : TValue;

@UnionSyntheticType(options = ["alignment", "auto"])
sealed interface TAlignmentOrAuto : TValue, 
    TAlignmentOrAutoOrNone, 
    TAlignmentOrArrayOrAutoOrFunction<Nothing>;

@UnionSyntheticType(options = ["content", "label", "none"])
sealed interface TContentOrLabelOrNone : TValue;

@UnionSyntheticType(options = ["array", "auto", "fraction", "int", "relative"])
sealed interface TArrayOrAutoOrFractionOrIntOrRelative<out E : TValue> : TValue;

@UnionSyntheticType(options = ["auto", "fraction", "int", "relative"])
sealed interface TAutoOrFractionOrIntOrRelative : TValue, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>;

@UnionSyntheticType(options = ["array", "color", "function", "gradient", "none", "pattern"])
sealed interface TArrayOrColorOrFunctionOrGradientOrNoneOrPattern<out E : TValue> : TValue, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<E, Nothing>;

@UnionSyntheticType(options = ["alignment", "array", "auto", "function"])
sealed interface TAlignmentOrArrayOrAutoOrFunction<out E : TValue> : TValue;

@UnionSyntheticType(options = ["array", "color", "dictionary", "function", "gradient", "length", "none", "pattern", "stroke"])
sealed interface TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<out E : TValue, out V : TValue> : TValue;

@UnionSyntheticType(options = ["array", "fraction", "function", "relative", "sides"])
sealed interface TArrayOrFractionOrFunctionOrRelativeOrSides<out E : TValue, out S : TValue> : TValue;

@UnionSyntheticType(options = ["fraction", "relative"])
sealed interface TFractionOrRelative : TValue, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TAutoOrFractionOrIntOrRelative, 
    TArrayOrFractionOrFunctionOrRelativeOrSides<Nothing, Nothing>, 
    TAutoOrFractionOrRelative, 
    TContentOrFractionOrRelative, 
    TFractionOrNoneOrRelative;

@UnionSyntheticType(options = ["auto", "color", "gradient", "none", "pattern"])
sealed interface TAutoOrColorOrGradientOrNoneOrPattern : TValue, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>;

@UnionSyntheticType(options = ["auto", "relative", "sides"])
sealed interface TAutoOrRelativeOrSides<out S : TValue> : TValue;

@UnionSyntheticType(options = ["auto", "relative"])
sealed interface TAutoOrRelative : TValue, 
    TAutoOrBoolOrFunctionOrNoneOrRelative, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TAutoOrFractionOrIntOrRelative, 
    TAutoOrRelativeOrSides<Nothing>, 
    TAutoOrFractionOrRelative, 
    TAutoOrDictionaryOrRelative<Nothing>;

@UnionSyntheticType(options = ["color", "dictionary", "gradient", "length", "none", "pattern", "stroke"])
sealed interface TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<out V : TValue> : TValue, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, V>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<V>;

@UnionSyntheticType(options = ["array", "content"])
sealed interface TArrayOrContent<out E : TValue> : TValue, 
    TArrayOrContentOrFunction<E>;

@UnionSyntheticType(options = ["color", "gradient", "none", "pattern"])
sealed interface TColorOrGradientOrNoneOrPattern : TValue, 
    TArrayOrColorOrFunctionOrGradientOrNoneOrPattern<Nothing>, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, Nothing>, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>;

@UnionSyntheticType(options = ["highlight-top-edge", "length"])
sealed interface THighlightTopEdgeOrLength : TValue;

@UnionSyntheticType(options = ["highlight-bottom-edge", "length"])
sealed interface THighlightBottomEdgeOrLength : TValue;

@UnionSyntheticType(options = ["dictionary", "relative"])
sealed interface TDictionaryOrRelative<out V : TValue> : TValue, 
    TArrayOrDictionaryOrFunctionOrRelative<Nothing, V>, 
    TAutoOrDictionaryOrRelative<V>;

@UnionSyntheticType(options = ["auto", "color", "dictionary", "gradient", "length", "pattern", "stroke"])
sealed interface TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<out V : TValue> : TValue, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<V>;

@UnionSyntheticType(options = ["none", "str"])
sealed interface TNoneOrStr : TValue, 
    TFunctionOrNoneOrStr, 
    TAutoOrNoneOrStr, 
    TArrayOrNoneOrStr<Nothing>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>;

@UnionSyntheticType(options = ["auto", "none", "str"])
sealed interface TAutoOrNoneOrStr : TValue, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>;

@UnionSyntheticType(options = ["array", "auto", "dictionary", "str"])
sealed interface TArrayOrAutoOrDictionaryOrStr<out E : TValue, out V : TValue> : TValue, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, V>;

@UnionSyntheticType(options = ["int", "text-weight"])
sealed interface TIntOrTextWeight : TValue;

@UnionSyntheticType(options = ["color", "gradient", "pattern"])
sealed interface TColorOrGradientOrPattern : TValue, 
    TArrayOrColorOrFunctionOrGradientOrNoneOrPattern<Nothing>, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, Nothing>, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TColorOrGradientOrNoneOrPattern, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrGradientOrPattern;

@UnionSyntheticType(options = ["auto", "none"])
sealed interface TAutoOrNone : TValue, 
    TAutoOrContentOrNone, 
    TAutoOrDatetimeOrNone, 
    TAlignmentOrAutoOrNone, 
    TAutoOrContentOrFunctionOrNone, 
    TAutoOrBoolOrFunctionOrNoneOrRelative, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TAutoOrNoneOrStr, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>;

@UnionSyntheticType(options = ["length", "text-top-edge"])
sealed interface TLengthOrTextTopEdge : TValue;

@UnionSyntheticType(options = ["length", "text-bottom-edge"])
sealed interface TLengthOrTextBottomEdge : TValue;

@UnionSyntheticType(options = ["auto", "str"])
sealed interface TAutoOrStr : TValue, 
    TAutoOrFunctionOrStr, 
    TAutoOrNoneOrStr, 
    TArrayOrAutoOrDictionaryOrStr<Nothing, Nothing>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>;

@UnionSyntheticType(options = ["auto", "direction"])
sealed interface TAutoOrDirection : TValue;

@UnionSyntheticType(options = ["array", "int", "none"])
sealed interface TArrayOrIntOrNone<out E : TValue> : TValue;

@UnionSyntheticType(options = ["auto", "text-number-type"])
sealed interface TAutoOrTextNumberType : TValue;

@UnionSyntheticType(options = ["auto", "text-number-width"])
sealed interface TAutoOrTextNumberWidth : TValue;

@UnionSyntheticType(options = ["array", "dictionary"])
sealed interface TArrayOrDictionary<out E : TValue, out V : TValue> : TValue, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<E, V>, 
    TArrayOrAutoOrDictionaryOrStr<E, V>, 
    TArrayOrDictionaryOrFunctionOrRelative<E, V>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, V>;

@UnionSyntheticType(options = ["content", "str"])
sealed interface TContentOrStr : TValue;

@UnionSyntheticType(options = ["angle", "auto", "function"])
sealed interface TAngleOrAutoOrFunction : TValue;

@UnionSyntheticType(options = ["color", "dictionary", "gradient", "length", "pattern", "stroke"])
sealed interface TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<out V : TValue> : TValue, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, V>, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<V>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<V>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<V>;

@UnionSyntheticType(options = ["array", "none", "str"])
sealed interface TArrayOrNoneOrStr<out E : TValue> : TValue, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, Nothing>;

@UnionSyntheticType(options = ["dictionary", "int", "none"])
sealed interface TDictionaryOrIntOrNone<out V : TValue> : TValue;

@UnionSyntheticType(options = ["auto", "fraction", "relative"])
sealed interface TAutoOrFractionOrRelative : TValue, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TAutoOrFractionOrIntOrRelative;

@UnionSyntheticType(options = ["array", "dictionary", "function", "relative"])
sealed interface TArrayOrDictionaryOrFunctionOrRelative<out E : TValue, out V : TValue> : TValue;

@UnionSyntheticType(options = ["auto", "dictionary", "relative"])
sealed interface TAutoOrDictionaryOrRelative<out V : TValue> : TValue;

@UnionSyntheticType(options = ["none", "pagebreak-to"])
sealed interface TNoneOrPagebreakTo : TValue;

@UnionSyntheticType(options = ["auto", "length", "ratio"])
sealed interface TAutoOrLengthOrRatio : TValue;

@UnionSyntheticType(options = ["content", "fraction", "relative"])
sealed interface TContentOrFractionOrRelative : TValue;

@UnionSyntheticType(options = ["fraction", "none", "relative"])
sealed interface TFractionOrNoneOrRelative : TValue;

@UnionSyntheticType(options = ["auto", "color", "dictionary", "gradient", "length", "none", "pattern", "stroke"])
sealed interface TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<out V : TValue> : TValue;

@UnionSyntheticType(options = ["auto", "image-format"])
sealed interface TAutoOrImageFormat : TValue;

@UnionSyntheticType(options = ["array", "none"])
sealed interface TArrayOrNone<out E : TValue> : TValue, 
    TArrayOrColorOrFunctionOrGradientOrNoneOrPattern<E>, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<E, Nothing>, 
    TArrayOrIntOrNone<E>, 
    TArrayOrNoneOrStr<E>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, Nothing>;

@UnionSyntheticType(options = ["int", "ratio"])
sealed interface TIntOrRatio : TValue;

@UnionSyntheticType(options = ["float", "ratio"])
sealed interface TFloatOrRatio : TValue;

@UnionSyntheticType(options = ["array", "color"])
sealed interface TArrayOrColor<out E : TValue> : TValue, 
    TArrayOrColorOrFunctionOrGradientOrNoneOrPattern<E>, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<E, Nothing>;

@UnionSyntheticType(options = ["color", "ratio"])
sealed interface TColorOrRatio : TValue;

@UnionSyntheticType(options = ["array", "auto"])
sealed interface TArrayOrAuto<out E : TValue> : TValue, 
    TArrayOrAutoOrFractionOrIntOrRelative<E>, 
    TAlignmentOrArrayOrAutoOrFunction<E>, 
    TArrayOrAutoOrDictionaryOrStr<E, Nothing>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, Nothing>;

@UnionSyntheticType(options = ["auto", "color", "gradient", "pattern"])
sealed interface TAutoOrColorOrGradientOrPattern : TValue, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>;

@UnionSyntheticType(options = ["array", "auto", "dictionary", "none", "str"])
sealed interface TArrayOrAutoOrDictionaryOrNoneOrStr<out E : TValue, out V : TValue> : TValue;

@UnionSyntheticType(options = ["auto", "float"])
sealed interface TAutoOrFloat : TValue;

@UnionSyntheticType(options = ["none", "selector"])
sealed interface TNoneOrSelector : TValue;

@UnionSyntheticType(options = ["function", "label", "location", "selector", "str"])
sealed interface TFunctionOrLabelOrLocationOrSelectorOrStr : TValue;

@UnionSyntheticType(options = ["array", "function", "int"])
sealed interface TArrayOrFunctionOrInt<out E : TValue> : TValue;

@UnionSyntheticType(options = ["int", "length"])
sealed interface TIntOrLength : TValue;

