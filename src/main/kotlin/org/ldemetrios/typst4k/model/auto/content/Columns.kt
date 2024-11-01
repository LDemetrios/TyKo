package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TColumns(
	val count: TInt? = null,
	val body: TContent,
	val gutter: TRelative? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"columns",
		ArgumentEntry(false, null, count),
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "gutter", gutter),
	)
	override fun func() = TColumns
    companion object : TElement("columns") {
        internal val countType : InternalType = ConcreteType("int")
        internal val bodyType : InternalType = ConcreteType("content")
        internal val gutterType : InternalType = ConcreteType("relative")

    }
}

data class TSetColumns(
	val count: TInt? = null,
	val gutter: TRelative? = null,
) : TSetRule("columns") {
    override fun format() = Representations.setRepr(
		"columns",
		ArgumentEntry(false, null, count),
		ArgumentEntry(false, "gutter", gutter),
	)
}

