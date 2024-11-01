package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TLinebreak(
	val justify: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"linebreak",
		ArgumentEntry(false, "justify", justify),
	)
	override fun func() = TLinebreak
    companion object : TElement("linebreak") {
        internal val justifyType : InternalType = ConcreteType("bool")

    }
}

data class TSetLinebreak(
	val justify: TBool? = null,
) : TSetRule("linebreak") {
    override fun format() = Representations.setRepr(
		"linebreak",
		ArgumentEntry(false, "justify", justify),
	)
}

