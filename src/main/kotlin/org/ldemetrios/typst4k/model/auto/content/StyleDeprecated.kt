package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TStyleDeprecated(
	val func: TFunction,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.reprOf(this)
	override fun func() = TStyleDeprecated
    companion object : TElement("style-deprecated") {
        internal val funcType : InternalType = ConcreteType("function")

    }
}

