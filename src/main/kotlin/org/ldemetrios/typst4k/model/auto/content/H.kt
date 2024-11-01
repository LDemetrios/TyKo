package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TH(
	val amount: TFractionOrRelative,
	val weak: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"h",
		ArgumentEntry(false, null, amount),
		ArgumentEntry(false, "weak", weak),
	)
	override fun func() = TH
    companion object : TElement("h") {
        internal val amountType : InternalType = UnionType(ConcreteType("fraction"), ConcreteType("relative"))
        internal val weakType : InternalType = ConcreteType("bool")

    }
}

data class TSetH(
	val weak: TBool? = null,
) : TSetRule("h") {
    override fun format() = Representations.setRepr(
		"h",
		ArgumentEntry(false, "weak", weak),
	)
}

