package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TTableFooter(
	val children: TArray<TContent>,
	val repeat: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"table.footer",
		ArgumentEntry(true, null, children),
		ArgumentEntry(false, "repeat", repeat),
	)
	override fun func() = TTableFooter
    companion object : TElement("table.footer") {
        internal val childrenType : InternalType = ConcreteType("array", listOf(ConcreteType("content")))
        internal val repeatType : InternalType = ConcreteType("bool")

    }
}

data class TSetTableFooter(
	val repeat: TBool? = null,
) : TSetRule("table.footer") {
    override fun format() = Representations.setRepr(
		"table.footer",
		ArgumentEntry(false, "repeat", repeat),
	)
}

