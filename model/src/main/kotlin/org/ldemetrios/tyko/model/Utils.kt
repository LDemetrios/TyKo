package org.ldemetrios.tyko.model

fun <K, V : Any> mapOfNotNullValues(vararg pairs: Pair<K, V?>): Map<K, V> =
    pairs.mapNotNull { it.second?.run { it.first to this } }.toMap()

inline fun <reified T : Any> Any?.castOrNull() = this as? T
inline fun <reified T> Any?.cast() = this as T