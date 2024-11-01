package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TSuper(
	val body: TContent,
	val typographic: TBool? = null,
	val baseline: TLength? = null,
	val size: TLength? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"super",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "typographic", typographic),
		ArgumentEntry(false, "baseline", baseline),
		ArgumentEntry(false, "size", size),
	)
	override fun func() = TSuper
    companion object : TElement("super") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val typographicType : InternalType = ConcreteType("bool")
        internal val baselineType : InternalType = ConcreteType("length")
        internal val sizeType : InternalType = ConcreteType("length")

    }
}

data class TSetSuper(
	val typographic: TBool? = null,
	val baseline: TLength? = null,
	val size: TLength? = null,
) : TSetRule("super") {
    override fun format() = Representations.setRepr(
		"super",
		ArgumentEntry(false, "typographic", typographic),
		ArgumentEntry(false, "baseline", baseline),
		ArgumentEntry(false, "size", size),
	)
}

