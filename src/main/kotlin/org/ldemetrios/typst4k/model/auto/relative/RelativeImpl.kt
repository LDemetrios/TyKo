package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TRelativeImpl(
	val rel: TRatio? = null,
	val abs: TLength? = null,
) : TRelative {
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TRelativeImpl
    companion object : TType("relative-impl") {
        internal val relType : InternalType = ConcreteType("ratio")
        internal val absType : InternalType = ConcreteType("length")

    }
}
