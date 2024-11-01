package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TPar(
	val body: TContent,
	val leading: TLength? = null,
	val spacing: TLength? = null,
	val justify: TBool? = null,
	val linebreaks: TAutoOrParLinebreaks? = null,
	val firstLineIndent: TLength? = null,
	val hangingIndent: TLength? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"par",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "leading", leading),
		ArgumentEntry(false, "spacing", spacing),
		ArgumentEntry(false, "justify", justify),
		ArgumentEntry(false, "linebreaks", linebreaks),
		ArgumentEntry(false, "first-line-indent", firstLineIndent),
		ArgumentEntry(false, "hanging-indent", hangingIndent),
	)
	override fun func() = TPar
    companion object : TElement("par") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val leadingType : InternalType = ConcreteType("length")
        internal val spacingType : InternalType = ConcreteType("length")
        internal val justifyType : InternalType = ConcreteType("bool")
        internal val linebreaksType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("par-linebreaks"))
        internal val firstLineIndentType : InternalType = ConcreteType("length")
        internal val hangingIndentType : InternalType = ConcreteType("length")

    }
}

data class TSetPar(
	val leading: TLength? = null,
	val spacing: TLength? = null,
	val justify: TBool? = null,
	val linebreaks: TAutoOrParLinebreaks? = null,
	val firstLineIndent: TLength? = null,
	val hangingIndent: TLength? = null,
) : TSetRule("par") {
    override fun format() = Representations.setRepr(
		"par",
		ArgumentEntry(false, "leading", leading),
		ArgumentEntry(false, "spacing", spacing),
		ArgumentEntry(false, "justify", justify),
		ArgumentEntry(false, "linebreaks", linebreaks),
		ArgumentEntry(false, "first-line-indent", firstLineIndent),
		ArgumentEntry(false, "hanging-indent", hangingIndent),
	)
}

