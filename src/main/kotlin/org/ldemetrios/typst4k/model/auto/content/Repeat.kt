package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TRepeat(
	val body: TContent,
	val gap: TLength? = null,
	val justify: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"repeat",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "gap", gap),
		ArgumentEntry(false, "justify", justify),
	)
	override fun func() = TRepeat
    companion object : TElement("repeat") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val gapType : InternalType = ConcreteType("length")
        internal val justifyType : InternalType = ConcreteType("bool")

    }
}

data class TSetRepeat(
	val gap: TLength? = null,
	val justify: TBool? = null,
) : TSetRule("repeat") {
    override fun format() = Representations.setRepr(
		"repeat",
		ArgumentEntry(false, "gap", gap),
		ArgumentEntry(false, "justify", justify),
	)
}

