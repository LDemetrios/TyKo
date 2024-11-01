package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TGridHeader(
	val children: TArray<TContent>,
	val repeat: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"grid.header",
		ArgumentEntry(true, null, children),
		ArgumentEntry(false, "repeat", repeat),
	)
	override fun func() = TGridHeader
    companion object : TElement("grid.header") {
        internal val childrenType : InternalType = ConcreteType("array", listOf(ConcreteType("content")))
        internal val repeatType : InternalType = ConcreteType("bool")

    }
}

data class TSetGridHeader(
	val repeat: TBool? = null,
) : TSetRule("grid.header") {
    override fun format() = Representations.setRepr(
		"grid.header",
		ArgumentEntry(false, "repeat", repeat),
	)
}

