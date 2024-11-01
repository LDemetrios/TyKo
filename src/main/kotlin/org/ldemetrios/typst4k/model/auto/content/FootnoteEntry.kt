package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TFootnoteEntry(
	val note: TContent,
	val separator: TContent? = null,
	val clearance: TLength? = null,
	val gap: TLength? = null,
	val indent: TLength? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"footnote.entry",
		ArgumentEntry(false, null, note),
		ArgumentEntry(false, "separator", separator),
		ArgumentEntry(false, "clearance", clearance),
		ArgumentEntry(false, "gap", gap),
		ArgumentEntry(false, "indent", indent),
	)
	override fun func() = TFootnoteEntry
    companion object : TElement("footnote.entry") {
        internal val noteType : InternalType = ConcreteType("content")
        internal val separatorType : InternalType = ConcreteType("content")
        internal val clearanceType : InternalType = ConcreteType("length")
        internal val gapType : InternalType = ConcreteType("length")
        internal val indentType : InternalType = ConcreteType("length")

    }
}

data class TSetFootnoteEntry(
	val separator: TContent? = null,
	val clearance: TLength? = null,
	val gap: TLength? = null,
	val indent: TLength? = null,
) : TSetRule("footnote.entry") {
    override fun format() = Representations.setRepr(
		"footnote.entry",
		ArgumentEntry(false, "separator", separator),
		ArgumentEntry(false, "clearance", clearance),
		ArgumentEntry(false, "gap", gap),
		ArgumentEntry(false, "indent", indent),
	)
}

