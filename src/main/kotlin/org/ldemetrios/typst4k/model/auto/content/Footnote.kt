package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TFootnote(
	val body: TContentOrLabel,
	val numbering: TFunctionOrStr? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"footnote",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "numbering", numbering),
	)
	override fun func() = TFootnote
    companion object : TElement("footnote") {
        internal val bodyType : InternalType = UnionType(ConcreteType("content"), ConcreteType("label"))
        internal val numberingType : InternalType = UnionType(ConcreteType("function"), ConcreteType("str"))

    }
}

data class TSetFootnote(
	val numbering: TFunctionOrStr? = null,
) : TSetRule("footnote") {
    override fun format() = Representations.setRepr(
		"footnote",
		ArgumentEntry(false, "numbering", numbering),
	)
}

