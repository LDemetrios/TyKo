package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TEnumItem(
	val number: TIntOrNone? = null,
	val body: TContent,
	override val label:TLabel? = null,
) : TContent(), 
    TArrayOrEnumItem<Nothing> {
    override fun format() = Representations.elementRepr(
		"enum.item",
		ArgumentEntry(false, null, number),
		ArgumentEntry(false, null, body),
	)
	override fun func() = TEnumItem
    companion object : TElement("enum.item") {
        internal val numberType : InternalType = UnionType(ConcreteType("int"), ConcreteType("none"))
        internal val bodyType : InternalType = ConcreteType("content")

    }
}

data class TSetEnumItem(
	val number: TIntOrNone? = null,
) : TSetRule("enum.item") {
    override fun format() = Representations.setRepr(
		"enum.item",
		ArgumentEntry(false, null, number),
	)
}

