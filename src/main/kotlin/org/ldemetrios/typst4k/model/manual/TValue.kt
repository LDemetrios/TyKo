package org.ldemetrios.typst4k.model

sealed interface TValue {
    fun type(): TType
    fun format(): String
}

