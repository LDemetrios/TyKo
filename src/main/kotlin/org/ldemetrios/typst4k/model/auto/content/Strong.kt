package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TStrong(
	val body: TContent,
	val delta: TInt? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"strong",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "delta", delta),
	)
	override fun func() = TStrong
    companion object : TElement("strong") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val deltaType : InternalType = ConcreteType("int")

    }
}

data class TSetStrong(
	val delta: TInt? = null,
) : TSetRule("strong") {
    override fun format() = Representations.setRepr(
		"strong",
		ArgumentEntry(false, "delta", delta),
	)
}

