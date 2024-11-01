package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@SerialName("array")
@Serializable
data class TArray<out E : TValue>(@Serializable(with = CustomListSerializer::class) val value : List<E>) : TValue, List<E> by value, 
    TArrayOrColor<E>, 
    TArrayOrAuto<E>, 
    TArrayOrStr<E>, 
    TArrayOrAutoOrDictionaryOrStr<E, Nothing>, 
    TArrayOrAutoOrFractionOrIntOrRelative<E>, 
    TArrayOrColorOrGradientOrNoneOrPattern<E>, 
    TAlignmentOrArrayOrAuto<E>, 
    TArrayOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<E, Nothing>, 
    TArrayOrDictionaryOrRelative<E, Nothing>, 
    TArrayOrIntOrNone<E>, 
    TArrayOrDictionary<E, Nothing>, 
    TArrayOrContent<E>, 
    TArrayOrNoneOrStr<E>, 
    TArrayOrNone<E>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<E, Nothing> {
    override fun repr() : String = RT.reprOf(value)
    override fun toString() : String = value.toString()
}
