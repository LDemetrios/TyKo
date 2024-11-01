package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TQuote(
	val body: TContent,
	val block: TBool? = null,
	val quotes: TAutoOrBool? = null,
	val attribution: TContentOrLabelOrNone? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"quote",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "block", block),
		ArgumentEntry(false, "quotes", quotes),
		ArgumentEntry(false, "attribution", attribution),
	)
	override fun func() = TQuote
    companion object : TElement("quote") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val blockType : InternalType = ConcreteType("bool")
        internal val quotesType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("bool"))
        internal val attributionType : InternalType = UnionType(ConcreteType("content"), ConcreteType("label"), ConcreteType("none"))

    }
}

data class TSetQuote(
	val block: TBool? = null,
	val quotes: TAutoOrBool? = null,
	val attribution: TContentOrLabelOrNone? = null,
) : TSetRule("quote") {
    override fun format() = Representations.setRepr(
		"quote",
		ArgumentEntry(false, "block", block),
		ArgumentEntry(false, "quotes", quotes),
		ArgumentEntry(false, "attribution", attribution),
	)
}

