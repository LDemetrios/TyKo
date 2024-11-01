package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TTermsItem(
	val term: TContent,
	val description: TContent,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"terms.item",
		ArgumentEntry(false, null, term),
		ArgumentEntry(false, null, description),
	)
	override fun func() = TTermsItem
    companion object : TElement("terms.item") {
        internal val termType : InternalType = ConcreteType("content")
        internal val descriptionType : InternalType = ConcreteType("content")

    }
}

