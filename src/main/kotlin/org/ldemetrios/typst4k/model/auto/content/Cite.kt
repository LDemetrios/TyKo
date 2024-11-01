package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TCite(
	val key: TLabel,
	val supplement: TContentOrNone? = null,
	val form: TCiteFormOrNone? = null,
	val style: TAutoOrCiteStyle? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"cite",
		ArgumentEntry(false, null, key),
		ArgumentEntry(false, "supplement", supplement),
		ArgumentEntry(false, "form", form),
		ArgumentEntry(false, "style", style),
	)
	override fun func() = TCite
    companion object : TElement("cite") {
        internal val keyType : InternalType = ConcreteType("label")
        internal val supplementType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val formType : InternalType = UnionType(ConcreteType("cite-form"), ConcreteType("none"))
        internal val styleType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("cite-style"))

    }
}

data class TSetCite(
	val supplement: TContentOrNone? = null,
	val form: TCiteFormOrNone? = null,
	val style: TAutoOrCiteStyle? = null,
) : TSetRule("cite") {
    override fun format() = Representations.setRepr(
		"cite",
		ArgumentEntry(false, "supplement", supplement),
		ArgumentEntry(false, "form", form),
		ArgumentEntry(false, "style", style),
	)
}

