package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TGridFooter(
	val children: TArray<TContent>,
	val repeat: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"grid.footer",
		ArgumentEntry(true, null, children),
		ArgumentEntry(false, "repeat", repeat),
	)
	override fun func() = TGridFooter
    companion object : TElement("grid.footer") {
        internal val childrenType : InternalType = ConcreteType("array", listOf(ConcreteType("content")))
        internal val repeatType : InternalType = ConcreteType("bool")

    }
}

data class TSetGridFooter(
	val repeat: TBool? = null,
) : TSetRule("grid.footer") {
    override fun format() = Representations.setRepr(
		"grid.footer",
		ArgumentEntry(false, "repeat", repeat),
	)
}

