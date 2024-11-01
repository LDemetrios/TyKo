package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathRoot(
	val index: TContentOrNone? = null,
	val radicand: TContent,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.reprOf(this)
	override fun func() = TMathRoot
    companion object : TElement("math.root") {
        internal val indexType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val radicandType : InternalType = ConcreteType("content")

    }
}

data class TSetMathRoot(
	val index: TContentOrNone? = null,
) : TSetRule("math.root") {
    override fun format() = Representations.setRepr(
		"math.root",
		ArgumentEntry(false, null, index),
	)
}

