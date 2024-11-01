package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TPage(
	val body: TContent,
	val paper: TPagePaper? = null,
	val width: TAutoOrLength? = null,
	val height: TAutoOrLength? = null,
	val flipped: TBool? = null,
	val margin: TAutoOrDictionaryOrRelative<*, >? = null,
	val binding: TAlignmentOrAuto? = null,
	val columns: TInt? = null,
	val fill: TAutoOrColorOrGradientOrNoneOrPattern? = null,
	val numbering: TFunctionOrNoneOrStr? = null,
	val numberAlign: TAlignment? = null,
	val header: TAutoOrContentOrNone? = null,
	val headerAscent: TRelative? = null,
	val footer: TAutoOrContentOrNone? = null,
	val footerDescent: TRelative? = null,
	val background: TContentOrNone? = null,
	val foreground: TContentOrNone? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"page",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "paper", paper),
		ArgumentEntry(false, "width", width),
		ArgumentEntry(false, "height", height),
		ArgumentEntry(false, "flipped", flipped),
		ArgumentEntry(false, "margin", margin),
		ArgumentEntry(false, "binding", binding),
		ArgumentEntry(false, "columns", columns),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "numbering", numbering),
		ArgumentEntry(false, "number-align", numberAlign),
		ArgumentEntry(false, "header", header),
		ArgumentEntry(false, "header-ascent", headerAscent),
		ArgumentEntry(false, "footer", footer),
		ArgumentEntry(false, "footer-descent", footerDescent),
		ArgumentEntry(false, "background", background),
		ArgumentEntry(false, "foreground", foreground),
	)
	override fun func() = TPage
    companion object : TElement("page") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val paperType : InternalType = ConcreteType("page-paper")
        internal val widthType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("length"))
        internal val heightType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("length"))
        internal val flippedType : InternalType = ConcreteType("bool")
        internal val marginType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("dictionary", listOf(AnyType)), ConcreteType("relative"))
        internal val bindingType : InternalType = UnionType(ConcreteType("alignment"), ConcreteType("auto"))
        internal val columnsType : InternalType = ConcreteType("int")
        internal val fillType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("color"), ConcreteType("gradient"), ConcreteType("none"), ConcreteType("pattern"))
        internal val numberingType : InternalType = UnionType(ConcreteType("function"), ConcreteType("none"), ConcreteType("str"))
        internal val numberAlignType : InternalType = ConcreteType("alignment")
        internal val headerType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("content"), ConcreteType("none"))
        internal val headerAscentType : InternalType = ConcreteType("relative")
        internal val footerType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("content"), ConcreteType("none"))
        internal val footerDescentType : InternalType = ConcreteType("relative")
        internal val backgroundType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val foregroundType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))

    }
}

data class TSetPage(
	val paper: TPagePaper? = null,
	val width: TAutoOrLength? = null,
	val height: TAutoOrLength? = null,
	val flipped: TBool? = null,
	val margin: TAutoOrDictionaryOrRelative<*, >? = null,
	val binding: TAlignmentOrAuto? = null,
	val columns: TInt? = null,
	val fill: TAutoOrColorOrGradientOrNoneOrPattern? = null,
	val numbering: TFunctionOrNoneOrStr? = null,
	val numberAlign: TAlignment? = null,
	val header: TAutoOrContentOrNone? = null,
	val headerAscent: TRelative? = null,
	val footer: TAutoOrContentOrNone? = null,
	val footerDescent: TRelative? = null,
	val background: TContentOrNone? = null,
	val foreground: TContentOrNone? = null,
) : TSetRule("page") {
    override fun format() = Representations.setRepr(
		"page",
		ArgumentEntry(false, "paper", paper),
		ArgumentEntry(false, "width", width),
		ArgumentEntry(false, "height", height),
		ArgumentEntry(false, "flipped", flipped),
		ArgumentEntry(false, "margin", margin),
		ArgumentEntry(false, "binding", binding),
		ArgumentEntry(false, "columns", columns),
		ArgumentEntry(false, "fill", fill),
		ArgumentEntry(false, "numbering", numbering),
		ArgumentEntry(false, "number-align", numberAlign),
		ArgumentEntry(false, "header", header),
		ArgumentEntry(false, "header-ascent", headerAscent),
		ArgumentEntry(false, "footer", footer),
		ArgumentEntry(false, "footer-descent", footerDescent),
		ArgumentEntry(false, "background", background),
		ArgumentEntry(false, "foreground", foreground),
	)
}

