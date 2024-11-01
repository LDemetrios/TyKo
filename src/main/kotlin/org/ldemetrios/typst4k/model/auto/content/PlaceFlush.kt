package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TPlaceFlush(
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"place.flush",
	)
	override fun func() = TPlaceFlush
    companion object : TElement("place.flush") {

    }
}

