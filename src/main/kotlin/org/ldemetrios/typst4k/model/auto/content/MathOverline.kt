package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathOverline(
	val body: TContent,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.overline",
		ArgumentEntry(false, null, body),
	)
	override fun func() = TMathOverline
    companion object : TElement("math.overline") {
        internal val bodyType : InternalType = ConcreteType("content")

    }
}

