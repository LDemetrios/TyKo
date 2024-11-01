package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

abstract class TDictionary<out V: TValue> : Map<String, V>, TValue, 
    TArrayOrColorOrDictionaryOrFunctionOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing, Nothing>, 
    TColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TDictionaryOrRelative<Nothing>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TArrayOrAutoOrDictionaryOrStr<Nothing, Nothing>, 
    TArrayOrDictionary<Nothing, Nothing>, 
    TColorOrDictionaryOrGradientOrLengthOrPatternOrStroke<Nothing>, 
    TDictionaryOrIntOrNone<Nothing>, 
    TArrayOrDictionaryOrFunctionOrRelative<Nothing, Nothing>, 
    TAutoOrDictionaryOrRelative<Nothing>, 
    TAutoOrColorOrDictionaryOrGradientOrLengthOrNoneOrPatternOrStroke<Nothing>, 
    TArrayOrAutoOrDictionaryOrNoneOrStr<Nothing, Nothing>{
    override fun type() : TType = TDictionary
    override fun format() = Representations.reprOf(this)
    companion object: TType("dictionary") {
    }
    abstract val value : Map<String, V>
    override val entries: Set<Map.Entry<String, V>> get() = value.entries
    override val keys: Set<String> get() = value.keys
    override val size: Int get() = value.size
    override val values: Collection<V> get() = value.values
    override fun containsKey(key: String): Boolean = value.containsKey(key)
    override fun containsValue(value: @UnsafeVariance V): Boolean = this.value.containsValue(value)
    override fun get(key: String): V? = value.get(key)
    override fun isEmpty(): Boolean = value.isEmpty()
    override fun forEach(action: java.util.function.BiConsumer<in String, in V>) = value.forEach(action)
    override fun getOrDefault(key: String, defaultValue: @UnsafeVariance V): V = value.getOrDefault(key, defaultValue)
}
