package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TPagebreak(
	val weak: TBool? = null,
	val to: TNoneOrPagebreakTo? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"pagebreak",
		ArgumentEntry(false, "weak", weak),
		ArgumentEntry(false, "to", to),
	)
	override fun func() = TPagebreak
    companion object : TElement("pagebreak") {
        internal val weakType : InternalType = ConcreteType("bool")
        internal val toType : InternalType = UnionType(ConcreteType("none"), ConcreteType("pagebreak-to"))

    }
}

data class TSetPagebreak(
	val weak: TBool? = null,
	val to: TNoneOrPagebreakTo? = null,
) : TSetRule("pagebreak") {
    override fun format() = Representations.setRepr(
		"pagebreak",
		ArgumentEntry(false, "weak", weak),
		ArgumentEntry(false, "to", to),
	)
}

