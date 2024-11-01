package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TPoint(
	val x: TLength,
	val y: TLength,
) : TDictionary<TLength>() {
	override val value: Map<String, TLength> get() = mapOf(
			"x" to x,
			"y" to y,
	)
    companion object  {
        internal val xType : InternalType = ConcreteType("length")
        internal val yType : InternalType = ConcreteType("length")

    }
}

