package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TSmartquote(
	val double: TBool? = null,
	val enabled: TBool? = null,
	val alternative: TBool? = null,
	val quotes: TArrayOrAutoOrDictionaryOrStr<*, *, >? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"smartquote",
		ArgumentEntry(false, "double", double),
		ArgumentEntry(false, "enabled", enabled),
		ArgumentEntry(false, "alternative", alternative),
		ArgumentEntry(false, "quotes", quotes),
	)
	override fun func() = TSmartquote
    companion object : TElement("smartquote") {
        internal val doubleType : InternalType = ConcreteType("bool")
        internal val enabledType : InternalType = ConcreteType("bool")
        internal val alternativeType : InternalType = ConcreteType("bool")
        internal val quotesType : InternalType = UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("auto"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("str"))

    }
}

data class TSetSmartquote(
	val double: TBool? = null,
	val enabled: TBool? = null,
	val alternative: TBool? = null,
	val quotes: TArrayOrAutoOrDictionaryOrStr<*, *, >? = null,
) : TSetRule("smartquote") {
    override fun format() = Representations.setRepr(
		"smartquote",
		ArgumentEntry(false, "double", double),
		ArgumentEntry(false, "enabled", enabled),
		ArgumentEntry(false, "alternative", alternative),
		ArgumentEntry(false, "quotes", quotes),
	)
}

