package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMathOp(
	val text: TContent,
	val limits: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"math.op",
		ArgumentEntry(false, null, text),
		ArgumentEntry(false, "limits", limits),
	)
	override fun func() = TMathOp
    companion object : TElement("math.op") {
        internal val textType : InternalType = ConcreteType("content")
        internal val limitsType : InternalType = ConcreteType("bool")

    }
}

data class TSetMathOp(
	val limits: TBool? = null,
) : TSetRule("math.op") {
    override fun format() = Representations.setRepr(
		"math.op",
		ArgumentEntry(false, "limits", limits),
	)
}

