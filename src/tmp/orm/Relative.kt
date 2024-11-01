package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@Serializable
sealed interface TRelative : TValue, 
    TFractionOrRelative, 
    TDictionaryOrRelative<Nothing>, 
    TAutoOrBoolOrNoneOrRelative, 
    TArrayOrAutoOrFractionOrIntOrRelative<Nothing>, 
    TArrayOrDictionaryOrRelative<Nothing, Nothing>, 
    TAutoOrFractionOrRelative, 
    TAutoOrRelative, 
    TAutoOrDictionaryOrRelative<Nothing>, 
    TContentOrFractionOrRelative, 
    TFractionOrNoneOrRelative
