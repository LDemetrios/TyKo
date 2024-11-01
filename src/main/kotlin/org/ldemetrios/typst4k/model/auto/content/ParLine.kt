package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TParLine(
	val numbering: TFunctionOrNoneOrStr? = null,
	val numberAlign: TAlignmentOrAuto? = null,
	val numberMargin: TAlignment? = null,
	val numberClearance: TAutoOrLength? = null,
	val numberingScope: TParLineNumberingScope? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"par.line",
		ArgumentEntry(false, "numbering", numbering),
		ArgumentEntry(false, "number-align", numberAlign),
		ArgumentEntry(false, "number-margin", numberMargin),
		ArgumentEntry(false, "number-clearance", numberClearance),
		ArgumentEntry(false, "numbering-scope", numberingScope),
	)
	override fun func() = TParLine
    companion object : TElement("par.line") {
        internal val numberingType : InternalType = UnionType(ConcreteType("function"), ConcreteType("none"), ConcreteType("str"))
        internal val numberAlignType : InternalType = UnionType(ConcreteType("alignment"), ConcreteType("auto"))
        internal val numberMarginType : InternalType = ConcreteType("alignment")
        internal val numberClearanceType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("length"))
        internal val numberingScopeType : InternalType = ConcreteType("par.line-numbering-scope")

    }
}

data class TSetParLine(
	val numbering: TFunctionOrNoneOrStr? = null,
	val numberAlign: TAlignmentOrAuto? = null,
	val numberMargin: TAlignment? = null,
	val numberClearance: TAutoOrLength? = null,
	val numberingScope: TParLineNumberingScope? = null,
) : TSetRule("par.line") {
    override fun format() = Representations.setRepr(
		"par.line",
		ArgumentEntry(false, "numbering", numbering),
		ArgumentEntry(false, "number-align", numberAlign),
		ArgumentEntry(false, "number-margin", numberMargin),
		ArgumentEntry(false, "number-clearance", numberClearance),
		ArgumentEntry(false, "numbering-scope", numberingScope),
	)
}

