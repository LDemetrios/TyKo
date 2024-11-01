package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TRawLine(
	val number: TInt,
	val count: TInt,
	val text: TStr,
	val body: TContent,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"raw.line",
		ArgumentEntry(false, null, number),
		ArgumentEntry(false, null, count),
		ArgumentEntry(false, null, text),
		ArgumentEntry(false, null, body),
	)
	override fun func() = TRawLine
    companion object : TElement("raw.line") {
        internal val numberType : InternalType = ConcreteType("int")
        internal val countType : InternalType = ConcreteType("int")
        internal val textType : InternalType = ConcreteType("str")
        internal val bodyType : InternalType = ConcreteType("content")

    }
}

