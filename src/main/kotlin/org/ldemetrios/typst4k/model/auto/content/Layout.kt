package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TLayout(
	val func: TFunction,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"layout",
		ArgumentEntry(false, "func", func),
	)
	override fun func() = TLayout
    companion object : TElement("layout") {
        internal val funcType : InternalType = ConcreteType("function")

    }
}

