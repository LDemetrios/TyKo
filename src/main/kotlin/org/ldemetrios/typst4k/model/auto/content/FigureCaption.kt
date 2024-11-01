package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TFigureCaption(
	val body: TContent,
	val position: TAlignment? = null,
	val separator: TAutoOrContent? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"figure.caption",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "position", position),
		ArgumentEntry(false, "separator", separator),
	)
	override fun func() = TFigureCaption
    companion object : TElement("figure.caption") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val positionType : InternalType = ConcreteType("alignment")
        internal val separatorType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("content"))

    }
}

data class TSetFigureCaption(
	val position: TAlignment? = null,
	val separator: TAutoOrContent? = null,
) : TSetRule("figure.caption") {
    override fun format() = Representations.setRepr(
		"figure.caption",
		ArgumentEntry(false, "position", position),
		ArgumentEntry(false, "separator", separator),
	)
}

