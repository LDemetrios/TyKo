package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TRef(
	val target: TLabel,
	val supplement: TAutoOrContentOrFunctionOrNone? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"ref",
		ArgumentEntry(false, null, target),
		ArgumentEntry(false, "supplement", supplement),
	)
	override fun func() = TRef
    companion object : TElement("ref") {
        internal val targetType : InternalType = ConcreteType("label")
        internal val supplementType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("content"), ConcreteType("function"), ConcreteType("none"))

    }
}

data class TSetRef(
	val supplement: TAutoOrContentOrFunctionOrNone? = null,
) : TSetRule("ref") {
    override fun format() = Representations.setRepr(
		"ref",
		ArgumentEntry(false, "supplement", supplement),
	)
}

