package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMove(
	val body: TContent,
	val dx: TRelative? = null,
	val dy: TRelative? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"move",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "dx", dx),
		ArgumentEntry(false, "dy", dy),
	)
	override fun func() = TMove
    companion object : TElement("move") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val dxType : InternalType = ConcreteType("relative")
        internal val dyType : InternalType = ConcreteType("relative")

    }
}

data class TSetMove(
	val dx: TRelative? = null,
	val dy: TRelative? = null,
) : TSetRule("move") {
    override fun format() = Representations.setRepr(
		"move",
		ArgumentEntry(false, "dx", dx),
		ArgumentEntry(false, "dy", dy),
	)
}

