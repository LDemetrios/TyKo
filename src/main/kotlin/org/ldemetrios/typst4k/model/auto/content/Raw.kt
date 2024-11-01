package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TRaw(
	val text: TStr,
	val block: TBool? = null,
	val lang: TNoneOrStr? = null,
	val align: TAlignment? = null,
	val syntaxes: TArrayOrStr<TStr, >? = null,
	val theme: TAutoOrNoneOrStr? = null,
	val tabSize: TInt? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"raw",
		ArgumentEntry(false, null, text),
		ArgumentEntry(false, "block", block),
		ArgumentEntry(false, "lang", lang),
		ArgumentEntry(false, "align", align),
		ArgumentEntry(false, "syntaxes", syntaxes),
		ArgumentEntry(false, "theme", theme),
		ArgumentEntry(false, "tab-size", tabSize),
	)
	override fun func() = TRaw
    companion object : TElement("raw") {
        internal val textType : InternalType = ConcreteType("str")
        internal val blockType : InternalType = ConcreteType("bool")
        internal val langType : InternalType = UnionType(ConcreteType("none"), ConcreteType("str"))
        internal val alignType : InternalType = ConcreteType("alignment")
        internal val syntaxesType : InternalType = UnionType(ConcreteType("array", listOf(ConcreteType("str"))), ConcreteType("str"))
        internal val themeType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("none"), ConcreteType("str"))
        internal val tabSizeType : InternalType = ConcreteType("int")

    }
}

data class TSetRaw(
	val block: TBool? = null,
	val lang: TNoneOrStr? = null,
	val align: TAlignment? = null,
	val syntaxes: TArrayOrStr<TStr, >? = null,
	val theme: TAutoOrNoneOrStr? = null,
	val tabSize: TInt? = null,
) : TSetRule("raw") {
    override fun format() = Representations.setRepr(
		"raw",
		ArgumentEntry(false, "block", block),
		ArgumentEntry(false, "lang", lang),
		ArgumentEntry(false, "align", align),
		ArgumentEntry(false, "syntaxes", syntaxes),
		ArgumentEntry(false, "theme", theme),
		ArgumentEntry(false, "tab-size", tabSize),
	)
}

