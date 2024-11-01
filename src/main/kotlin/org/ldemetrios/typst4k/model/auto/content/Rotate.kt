package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TRotate(
	val angle: TAngle? = null,
	val body: TContent,
	val origin: TAlignment? = null,
	val reflow: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"rotate",
		ArgumentEntry(false, null, angle),
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "origin", origin),
		ArgumentEntry(false, "reflow", reflow),
	)
	override fun func() = TRotate
    companion object : TElement("rotate") {
        internal val angleType : InternalType = ConcreteType("angle")
        internal val bodyType : InternalType = ConcreteType("content")
        internal val originType : InternalType = ConcreteType("alignment")
        internal val reflowType : InternalType = ConcreteType("bool")

    }
}

data class TSetRotate(
	val angle: TAngle? = null,
	val origin: TAlignment? = null,
	val reflow: TBool? = null,
) : TSetRule("rotate") {
    override fun format() = Representations.setRepr(
		"rotate",
		ArgumentEntry(false, null, angle),
		ArgumentEntry(false, "origin", origin),
		ArgumentEntry(false, "reflow", reflow),
	)
}

