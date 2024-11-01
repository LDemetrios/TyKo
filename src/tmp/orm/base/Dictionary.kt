package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("dictionary")
@Serializable
data class TDictionary<out V : TValue>(@Serializable(with = CustomMapSerializer::class) val value : Map<String, V>) : TValue, Map<String, V> by value, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<V>, 
    TDictionaryOrRelative<V>, 
    TDictionaryOrLabelOrLocationOrStr<V>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<V>, 
    TArrayOrAutoOrDictionaryOrStr<Nothing, V>, 
    TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, V>, 
    TArrayOrDictionaryOrRelative<Nothing, V>, 
    TArrayOrDictionary<Nothing, V>, 
    TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<V>, 
    TDictionaryOrIntOrNone<V>, 
    TAutoOrDictionaryOrRelative<V>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<V>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, V> {
    override fun repr() : String = RT.reprOf(value)
    override fun toString() : String = value.toString()
}
