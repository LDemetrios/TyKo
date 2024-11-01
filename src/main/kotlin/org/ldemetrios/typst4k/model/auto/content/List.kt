package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TList(
	val children: TArray<TListItem>,
	val tight: TBool? = null,
	val marker: TArrayOrContentOrFunction<TContent, >? = null,
	val indent: TLength? = null,
	val bodyIndent: TLength? = null,
	val spacing: TAutoOrLength? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"list",
		ArgumentEntry(true, null, children),
		ArgumentEntry(false, "tight", tight),
		ArgumentEntry(false, "marker", marker),
		ArgumentEntry(false, "indent", indent),
		ArgumentEntry(false, "body-indent", bodyIndent),
		ArgumentEntry(false, "spacing", spacing),
	)
	override fun func() = TList
    companion object : TElement("list") {
        internal val childrenType : InternalType = ConcreteType("array", listOf(ConcreteType("list.item")))
        internal val tightType : InternalType = ConcreteType("bool")
        internal val markerType : InternalType = UnionType(ConcreteType("array", listOf(ConcreteType("content"))), ConcreteType("content"), ConcreteType("function"))
        internal val indentType : InternalType = ConcreteType("length")
        internal val bodyIndentType : InternalType = ConcreteType("length")
        internal val spacingType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("length"))

    }
}

data class TSetList(
	val tight: TBool? = null,
	val marker: TArrayOrContentOrFunction<TContent, >? = null,
	val indent: TLength? = null,
	val bodyIndent: TLength? = null,
	val spacing: TAutoOrLength? = null,
) : TSetRule("list") {
    override fun format() = Representations.setRepr(
		"list",
		ArgumentEntry(false, "tight", tight),
		ArgumentEntry(false, "marker", marker),
		ArgumentEntry(false, "indent", indent),
		ArgumentEntry(false, "body-indent", bodyIndent),
		ArgumentEntry(false, "spacing", spacing),
	)
}

