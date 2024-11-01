package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TPad(
	val body: TContent,
	val left: TRelative? = null,
	val top: TRelative? = null,
	val right: TRelative? = null,
	val bottom: TRelative? = null,
	val x: TRelative? = null,
	val y: TRelative? = null,
	val rest: TRelative? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"pad",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "left", left),
		ArgumentEntry(false, "top", top),
		ArgumentEntry(false, "right", right),
		ArgumentEntry(false, "bottom", bottom),
		ArgumentEntry(false, "x", x),
		ArgumentEntry(false, "y", y),
		ArgumentEntry(false, "rest", rest),
	)
	override fun func() = TPad
    companion object : TElement("pad") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val leftType : InternalType = ConcreteType("relative")
        internal val topType : InternalType = ConcreteType("relative")
        internal val rightType : InternalType = ConcreteType("relative")
        internal val bottomType : InternalType = ConcreteType("relative")
        internal val xType : InternalType = ConcreteType("relative")
        internal val yType : InternalType = ConcreteType("relative")
        internal val restType : InternalType = ConcreteType("relative")

    }
}

data class TSetPad(
	val left: TRelative? = null,
	val top: TRelative? = null,
	val right: TRelative? = null,
	val bottom: TRelative? = null,
	val x: TRelative? = null,
	val y: TRelative? = null,
	val rest: TRelative? = null,
) : TSetRule("pad") {
    override fun format() = Representations.setRepr(
		"pad",
		ArgumentEntry(false, "left", left),
		ArgumentEntry(false, "top", top),
		ArgumentEntry(false, "right", right),
		ArgumentEntry(false, "bottom", bottom),
		ArgumentEntry(false, "x", x),
		ArgumentEntry(false, "y", y),
		ArgumentEntry(false, "rest", rest),
	)
}

