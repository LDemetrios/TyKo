package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TListItem(
	val body: TContent,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"list.item",
		ArgumentEntry(false, null, body),
	)
	override fun func() = TListItem
    companion object : TElement("list.item") {
        internal val bodyType : InternalType = ConcreteType("content")

    }
}

