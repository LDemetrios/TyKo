package org.ldemetrios.typst4k.orm

import kotlinx.serialization.*
import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.utilities.cast

@Serializable
sealed interface TContent : TValue, 
    TAutoOrContentOrNone, 
    TContentOrNone, 
    TContentOrLabel, 
    TContentOrLabelOrNone, 
    TArrayOrContent<Nothing>, 
    TContentOrStr, 
    TContentOrFractionOrRelative, 
    TAutoOrContent
