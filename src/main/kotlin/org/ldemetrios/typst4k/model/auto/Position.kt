package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TPosition(
	val page: TInt,
	val x: TLength,
	val y: TLength,
) : TDictionary<TIntOrLength>(), 
    TLabelOrLocationOrPositionOrStr {
	override val value: Map<String, TIntOrLength> get() = mapOf(
			"page" to page,
			"x" to x,
			"y" to y,
	)
    companion object  {
        internal val pageType : InternalType = ConcreteType("int")
        internal val xType : InternalType = ConcreteType("length")
        internal val yType : InternalType = ConcreteType("length")

    }
}

