package org.ldemetrios.typst4k.rt

import org.ldemetrios.typst4k.model.*
import org.ldemetrios.typst4k.model.*
import org.ldemetrios.typst4k.model.*
import org.ldemetrios.typst4k.model.*

val TRelative.abs: TLength?    get() = when(this) {
    is TRatio -> 0.pt
    is TLength -> this
    is TRelativeImpl -> this.abs
    else -> throw AssertionError()
}

val TRelative.rel : TRatio? get() = when(this) {
    is TRatio -> this
    is TLength -> 0.pc
    is TRelativeImpl -> this.rel
    else -> throw AssertionError()
}
