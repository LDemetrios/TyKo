package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TAlign(
	val alignment: TAlignment? = null,
	val body: TContent,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"align",
		ArgumentEntry(false, null, alignment),
		ArgumentEntry(false, null, body),
	)
	override fun func() = TAlign
    companion object : TElement("align") {
        internal val alignmentType : InternalType = ConcreteType("alignment")
        internal val bodyType : InternalType = ConcreteType("content")

    }
}

data class TSetAlign(
	val alignment: TAlignment? = null,
) : TSetRule("align") {
    override fun format() = Representations.setRepr(
		"align",
		ArgumentEntry(false, null, alignment),
	)
}

