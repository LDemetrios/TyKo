package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TEnum(
	val children: TArray<TArrayOrEnumItem<TEnumItem, >>,
	val tight: TBool? = null,
	val numbering: TFunctionOrStr? = null,
	val start: TInt? = null,
	val full: TBool? = null,
	val indent: TLength? = null,
	val bodyIndent: TLength? = null,
	val spacing: TAutoOrLength? = null,
	val numberAlign: TAlignment? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"enum",
		ArgumentEntry(true, null, children),
		ArgumentEntry(false, "tight", tight),
		ArgumentEntry(false, "numbering", numbering),
		ArgumentEntry(false, "start", start),
		ArgumentEntry(false, "full", full),
		ArgumentEntry(false, "indent", indent),
		ArgumentEntry(false, "body-indent", bodyIndent),
		ArgumentEntry(false, "spacing", spacing),
		ArgumentEntry(false, "number-align", numberAlign),
	)
	override fun func() = TEnum
    companion object : TElement("enum") {
        internal val childrenType : InternalType = ConcreteType("array", listOf(UnionType(ConcreteType("array", listOf(ConcreteType("enum.item"))), ConcreteType("enum.item"))))
        internal val tightType : InternalType = ConcreteType("bool")
        internal val numberingType : InternalType = UnionType(ConcreteType("function"), ConcreteType("str"))
        internal val startType : InternalType = ConcreteType("int")
        internal val fullType : InternalType = ConcreteType("bool")
        internal val indentType : InternalType = ConcreteType("length")
        internal val bodyIndentType : InternalType = ConcreteType("length")
        internal val spacingType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("length"))
        internal val numberAlignType : InternalType = ConcreteType("alignment")

    }
}

data class TSetEnum(
	val tight: TBool? = null,
	val numbering: TFunctionOrStr? = null,
	val start: TInt? = null,
	val full: TBool? = null,
	val indent: TLength? = null,
	val bodyIndent: TLength? = null,
	val spacing: TAutoOrLength? = null,
	val numberAlign: TAlignment? = null,
) : TSetRule("enum") {
    override fun format() = Representations.setRepr(
		"enum",
		ArgumentEntry(false, "tight", tight),
		ArgumentEntry(false, "numbering", numbering),
		ArgumentEntry(false, "start", start),
		ArgumentEntry(false, "full", full),
		ArgumentEntry(false, "indent", indent),
		ArgumentEntry(false, "body-indent", bodyIndent),
		ArgumentEntry(false, "spacing", spacing),
		ArgumentEntry(false, "number-align", numberAlign),
	)
}

