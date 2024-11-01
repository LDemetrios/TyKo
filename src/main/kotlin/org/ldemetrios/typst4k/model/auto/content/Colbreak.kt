package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TColbreak(
	val weak: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"colbreak",
		ArgumentEntry(false, "weak", weak),
	)
	override fun func() = TColbreak
    companion object : TElement("colbreak") {
        internal val weakType : InternalType = ConcreteType("bool")

    }
}

data class TSetColbreak(
	val weak: TBool? = null,
) : TSetRule("colbreak") {
    override fun format() = Representations.setRepr(
		"colbreak",
		ArgumentEntry(false, "weak", weak),
	)
}

