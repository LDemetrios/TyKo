package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class THide(
	val body: TContent,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"hide",
		ArgumentEntry(false, null, body),
	)
	override fun func() = THide
    companion object : TElement("hide") {
        internal val bodyType : InternalType = ConcreteType("content")

    }
}

