package org.ldemetrios.typst4k.model

class TDictionaryImpl<out V : TValue>(override val value: Map<String, V>) : TDictionary<V>()

class TStrImpl(override val value: String) : TStr

fun <V : TValue> TDictionary(value: Map<String, V>) = TDictionaryImpl(value)

fun TStr(value: String) = TStrImpl(value)
