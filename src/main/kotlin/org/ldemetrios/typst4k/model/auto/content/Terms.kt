package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TTerms(
	val children: TArray<TArrayOrContent<*, >>,
	val tight: TBool? = null,
	val separator: TContent? = null,
	val indent: TLength? = null,
	val hangingIndent: TLength? = null,
	val spacing: TAutoOrLength? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"terms",
		ArgumentEntry(true, null, children),
		ArgumentEntry(false, "tight", tight),
		ArgumentEntry(false, "separator", separator),
		ArgumentEntry(false, "indent", indent),
		ArgumentEntry(false, "hanging-indent", hangingIndent),
		ArgumentEntry(false, "spacing", spacing),
	)
	override fun func() = TTerms
    companion object : TElement("terms") {
        internal val childrenType : InternalType = ConcreteType("array", listOf(UnionType(ConcreteType("array", listOf(AnyType)), ConcreteType("content"))))
        internal val tightType : InternalType = ConcreteType("bool")
        internal val separatorType : InternalType = ConcreteType("content")
        internal val indentType : InternalType = ConcreteType("length")
        internal val hangingIndentType : InternalType = ConcreteType("length")
        internal val spacingType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("length"))

    }
}

data class TSetTerms(
	val tight: TBool? = null,
	val separator: TContent? = null,
	val indent: TLength? = null,
	val hangingIndent: TLength? = null,
	val spacing: TAutoOrLength? = null,
) : TSetRule("terms") {
    override fun format() = Representations.setRepr(
		"terms",
		ArgumentEntry(false, "tight", tight),
		ArgumentEntry(false, "separator", separator),
		ArgumentEntry(false, "indent", indent),
		ArgumentEntry(false, "hanging-indent", hangingIndent),
		ArgumentEntry(false, "spacing", spacing),
	)
}

