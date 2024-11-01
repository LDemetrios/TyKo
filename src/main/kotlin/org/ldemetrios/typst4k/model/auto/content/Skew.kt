package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TSkew(
	val body: TContent,
	val ax: TAngle? = null,
	val ay: TAngle? = null,
	val origin: TAlignment? = null,
	val reflow: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"skew",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "ax", ax),
		ArgumentEntry(false, "ay", ay),
		ArgumentEntry(false, "origin", origin),
		ArgumentEntry(false, "reflow", reflow),
	)
	override fun func() = TSkew
    companion object : TElement("skew") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val axType : InternalType = ConcreteType("angle")
        internal val ayType : InternalType = ConcreteType("angle")
        internal val originType : InternalType = ConcreteType("alignment")
        internal val reflowType : InternalType = ConcreteType("bool")

    }
}

data class TSetSkew(
	val ax: TAngle? = null,
	val ay: TAngle? = null,
	val origin: TAlignment? = null,
	val reflow: TBool? = null,
) : TSetRule("skew") {
    override fun format() = Representations.setRepr(
		"skew",
		ArgumentEntry(false, "ax", ax),
		ArgumentEntry(false, "ay", ay),
		ArgumentEntry(false, "origin", origin),
		ArgumentEntry(false, "reflow", reflow),
	)
}

