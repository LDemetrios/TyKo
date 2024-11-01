package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@Serializable
sealed interface TValue {
   fun repr() : String
}


@Serializable
sealed interface TIntOrRatio : TValue
@Serializable
sealed interface TFloatOrRatio : TValue
@Serializable
sealed interface TArrayOrColor<out E : TValue> : TValue, 
    TArrayOrColorOrGradientOrNoneOrPattern<E>, 
    TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<E, Nothing>
@Serializable
sealed interface TColorOrRatio : TValue
@Serializable
sealed interface TAutoOrStr : TValue, 
    TAutoOrNoneOrStr, 
    TArrayOrAutoOrDictionaryOrStr<Nothing, Nothing>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>
@Serializable
sealed interface TArrayOrAuto<out E : TValue> : TValue, 
    TArrayOrAutoOrDictionaryOrStr<E, Nothing>, 
    TArrayOrAutoOrFractionOrIntOrRelative<E>, 
    TAlignmentOrArrayOrAuto<E>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, Nothing>
@Serializable
sealed interface TArrayOrStr<out E : TValue> : TValue, 
    TArrayOrAutoOrDictionaryOrStr<E, Nothing>, 
    TArrayOrNoneOrStr<E>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, Nothing>
@Serializable
sealed interface TAutoOrContentOrNone : TValue
@Serializable
sealed interface TContentOrNone : TValue, 
    TAutoOrContentOrNone, 
    TContentOrLabelOrNone
@Serializable
sealed interface TNoneOrStr : TValue, 
    TAutoOrNoneOrStr, 
    TArrayOrNoneOrStr<Nothing>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>
@Serializable
sealed interface TAlignmentOrAutoOrNone : TValue
@Serializable
sealed interface TContentOrLabel : TValue, 
    TContentOrLabelOrNone
@Serializable
sealed interface TFractionOrRelative : TValue, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TAutoOrFractionOrRelative, 
    TContentOrFractionOrRelative, 
    TFractionOrNoneOrRelative
@Serializable
sealed interface TAutoOrInt : TValue, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>
@Serializable
sealed interface TAutoOrBool : TValue, 
    TAutoOrBoolOrNoneOrRelative
@Serializable
sealed interface TAutoOrLength : TValue, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TAutoOrLengthOrRatio, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>
@Serializable
sealed interface TColorOrGradientOrPattern : TValue, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TArrayOrColorOrGradientOrNoneOrPattern<Nothing>, 
    TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, Nothing>, 
    TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TColorOrGradientOrNoneOrPattern, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrGradientOrPattern
@Serializable
sealed interface TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<out V : TValue> : TValue, 
    TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, V>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<V>
@Serializable
sealed interface TLengthOrStr : TValue
@Serializable
sealed interface TDictionaryOrRelative<out V : TValue> : TValue, 
    TArrayOrDictionaryOrRelative<Nothing, V>, 
    TAutoOrDictionaryOrRelative<V>
@Serializable
sealed interface TDictionaryOrLabelOrLocationOrStr<out V : TValue> : TValue
@Serializable
sealed interface TIntOrLength : TValue
@Serializable
sealed interface TLabelOrLocationOrSelector : TValue
@Serializable
sealed interface TIntOrNone : TValue, 
    TArrayOrIntOrNone<Nothing>, 
    TDictionaryOrIntOrNone<Nothing>
@Serializable
sealed interface TAutoOrBoolOrNoneOrRelative : TValue
@Serializable
sealed interface TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<out V : TValue> : TValue, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<V>
@Serializable
sealed interface TAlignmentOrAuto : TValue, 
    TAlignmentOrAutoOrNone, 
    TAlignmentOrArrayOrAuto<Nothing>
@Serializable
sealed interface TContentOrLabelOrNone : TValue
@Serializable
sealed interface TAutoOrNoneOrStr : TValue, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>
@Serializable
sealed interface TArrayOrAutoOrDictionaryOrStr<out E : TValue, out V : TValue> : TValue, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, V>
@Serializable
sealed interface TArrayOrAutoOrFractionOrIntOrRelative<out E : TValue> : TValue
@Serializable
sealed interface TArrayOrColorOrGradientOrNoneOrPattern<out E : TValue> : TValue, 
    TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<E, Nothing>
@Serializable
sealed interface TAlignmentOrArrayOrAuto<out E : TValue> : TValue
@Serializable
sealed interface TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<out E : TValue, out V : TValue> : TValue
@Serializable
sealed interface TArrayOrDictionaryOrRelative<out E : TValue, out V : TValue> : TValue
@Serializable
sealed interface TIntOrStr : TValue
@Serializable
sealed interface TAutoOrNone : TValue, 
    TAutoOrContentOrNone, 
    TAlignmentOrAutoOrNone, 
    TAutoOrBoolOrNoneOrRelative, 
    TAutoOrNoneOrStr, 
    TAutoOrDatetimeOrNone, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>
@Serializable
sealed interface TAutoOrDirection : TValue
@Serializable
sealed interface TArrayOrIntOrNone<out E : TValue> : TValue
@Serializable
sealed interface TArrayOrDictionary<out E : TValue, out V : TValue> : TValue, 
    TArrayOrAutoOrDictionaryOrStr<E, V>, 
    TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<E, V>, 
    TArrayOrDictionaryOrRelative<E, V>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, V>
@Serializable
sealed interface TArrayOrContent<out E : TValue> : TValue
@Serializable
sealed interface TAutoOrFractionOrRelative : TValue, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>
@Serializable
sealed interface TAutoOrDatetimeOrNone : TValue
@Serializable
sealed interface TContentOrStr : TValue
@Serializable
sealed interface TAutoOrRelative : TValue, 
    TAutoOrBoolOrNoneOrRelative, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TAutoOrFractionOrRelative, 
    TAutoOrDictionaryOrRelative<Nothing>
@Serializable
sealed interface TAngleOrAuto : TValue
@Serializable
sealed interface TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<out V : TValue> : TValue, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<V>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<V>, 
    TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, V>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<V>
@Serializable
sealed interface TArrayOrNoneOrStr<out E : TValue> : TValue, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, Nothing>
@Serializable
sealed interface TDictionaryOrIntOrNone<out V : TValue> : TValue
@Serializable
sealed interface TColorOrGradientOrNoneOrPattern : TValue, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TArrayOrColorOrGradientOrNoneOrPattern<Nothing>, 
    TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, Nothing>, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>
@Serializable
sealed interface TAutoOrColorOrGradientOrNoneOrPattern : TValue, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>
@Serializable
sealed interface TAutoOrDictionaryOrRelative<out V : TValue> : TValue
@Serializable
sealed interface TAutoOrLengthOrRatio : TValue
@Serializable
sealed interface TContentOrFractionOrRelative : TValue
@Serializable
sealed interface TFractionOrNoneOrRelative : TValue
@Serializable
sealed interface TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<out V : TValue> : TValue
@Serializable
sealed interface TArrayOrNone<out E : TValue> : TValue, 
    TArrayOrColorOrGradientOrNoneOrPattern<E>, 
    TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<E, Nothing>, 
    TArrayOrIntOrNone<E>, 
    TArrayOrNoneOrStr<E>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, Nothing>
@Serializable
sealed interface TAutoOrColorOrGradientOrPattern : TValue, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TAutoOrColorOrGradientOrNoneOrPattern, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>
@Serializable
sealed interface TArrayOrAutoOrDictionaryOrNoneOrStr<out E : TValue, out V : TValue> : TValue
@Serializable
sealed interface TAutoOrFloat : TValue
@Serializable
sealed interface TAutoOrContent : TValue, 
    TAutoOrContentOrNone
