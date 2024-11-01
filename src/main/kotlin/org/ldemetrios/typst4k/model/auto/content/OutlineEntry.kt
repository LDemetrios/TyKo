package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TOutlineEntry(
	val level: TInt,
	val element: TContent,
	val body: TContent,
	val fill: TContentOrNone,
	val page: TContent,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"outline.entry",
		ArgumentEntry(false, null, level),
		ArgumentEntry(false, null, element),
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, null, fill),
		ArgumentEntry(false, null, page),
	)
	override fun func() = TOutlineEntry
    companion object : TElement("outline.entry") {
        internal val levelType : InternalType = ConcreteType("int")
        internal val elementType : InternalType = ConcreteType("content")
        internal val bodyType : InternalType = ConcreteType("content")
        internal val fillType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val pageType : InternalType = ConcreteType("content")

    }
}

