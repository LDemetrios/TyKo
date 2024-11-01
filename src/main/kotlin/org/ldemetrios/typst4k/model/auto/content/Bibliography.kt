package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TBibliography(
	val path: TArrayOrStr<TStr, >,
	val title: TAutoOrContentOrNone? = null,
	val full: TBool? = null,
	val style: TBibliographyStyle? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"bibliography",
		ArgumentEntry(false, null, path),
		ArgumentEntry(false, "title", title),
		ArgumentEntry(false, "full", full),
		ArgumentEntry(false, "style", style),
	)
	override fun func() = TBibliography
    companion object : TElement("bibliography") {
        internal val pathType : InternalType = UnionType(ConcreteType("array", listOf(ConcreteType("str"))), ConcreteType("str"))
        internal val titleType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("content"), ConcreteType("none"))
        internal val fullType : InternalType = ConcreteType("bool")
        internal val styleType : InternalType = ConcreteType("bibliography-style")

    }
}

data class TSetBibliography(
	val title: TAutoOrContentOrNone? = null,
	val full: TBool? = null,
	val style: TBibliographyStyle? = null,
) : TSetRule("bibliography") {
    override fun format() = Representations.setRepr(
		"bibliography",
		ArgumentEntry(false, "title", title),
		ArgumentEntry(false, "full", full),
		ArgumentEntry(false, "style", style),
	)
}

