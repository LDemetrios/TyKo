package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathClass(
	val clazz: TMathClassClass,
	val body: TContent,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.class",
		ArgumentEntry(false, null, clazz),
		ArgumentEntry(false, null, body),
	)
	override fun func() = TMathClass
    companion object : TElement("math.class") {
        internal val clazzType : InternalType = ConcreteType("math.class-class")
        internal val bodyType : InternalType = ConcreteType("content")

    }
}

