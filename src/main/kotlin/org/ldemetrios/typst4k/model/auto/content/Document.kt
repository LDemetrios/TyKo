package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TDocument(
	val title: TContentOrNone? = null,
	val author: TArrayOrStr<TStr, >? = null,
	val keywords: TArrayOrStr<TStr, >? = null,
	val date: TAutoOrDatetimeOrNone? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"document",
		ArgumentEntry(false, "title", title),
		ArgumentEntry(false, "author", author),
		ArgumentEntry(false, "keywords", keywords),
		ArgumentEntry(false, "date", date),
	)
	override fun func() = TDocument
    companion object : TElement("document") {
        internal val titleType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val authorType : InternalType = UnionType(ConcreteType("array", listOf(ConcreteType("str"))), ConcreteType("str"))
        internal val keywordsType : InternalType = UnionType(ConcreteType("array", listOf(ConcreteType("str"))), ConcreteType("str"))
        internal val dateType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("datetime"), ConcreteType("none"))

    }
}

data class TSetDocument(
	val title: TContentOrNone? = null,
	val author: TArrayOrStr<TStr, >? = null,
	val keywords: TArrayOrStr<TStr, >? = null,
	val date: TAutoOrDatetimeOrNone? = null,
) : TSetRule("document") {
    override fun format() = Representations.setRepr(
		"document",
		ArgumentEntry(false, "title", title),
		ArgumentEntry(false, "author", author),
		ArgumentEntry(false, "keywords", keywords),
		ArgumentEntry(false, "date", date),
	)
}

