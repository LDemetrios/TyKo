package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TFigure(
	val body: TContent,
	val placement: TAlignmentOrAutoOrNone? = null,
	val scope: TFigureScope? = null,
	val caption: TContentOrNone? = null,
	val kind: TAutoOrFunctionOrStr? = null,
	val supplement: TAutoOrContentOrFunctionOrNone? = null,
	val numbering: TFunctionOrNoneOrStr? = null,
	val gap: TLength? = null,
	val outlined: TBool? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"figure",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "placement", placement),
		ArgumentEntry(false, "scope", scope),
		ArgumentEntry(false, "caption", caption),
		ArgumentEntry(false, "kind", kind),
		ArgumentEntry(false, "supplement", supplement),
		ArgumentEntry(false, "numbering", numbering),
		ArgumentEntry(false, "gap", gap),
		ArgumentEntry(false, "outlined", outlined),
	)
	override fun func() = TFigure
    companion object : TElement("figure") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val placementType : InternalType = UnionType(ConcreteType("alignment"), ConcreteType("auto"), ConcreteType("none"))
        internal val scopeType : InternalType = ConcreteType("figure-scope")
        internal val captionType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))
        internal val kindType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("function"), ConcreteType("str"))
        internal val supplementType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("content"), ConcreteType("function"), ConcreteType("none"))
        internal val numberingType : InternalType = UnionType(ConcreteType("function"), ConcreteType("none"), ConcreteType("str"))
        internal val gapType : InternalType = ConcreteType("length")
        internal val outlinedType : InternalType = ConcreteType("bool")

    }
}

data class TSetFigure(
	val placement: TAlignmentOrAutoOrNone? = null,
	val scope: TFigureScope? = null,
	val caption: TContentOrNone? = null,
	val kind: TAutoOrFunctionOrStr? = null,
	val supplement: TAutoOrContentOrFunctionOrNone? = null,
	val numbering: TFunctionOrNoneOrStr? = null,
	val gap: TLength? = null,
	val outlined: TBool? = null,
) : TSetRule("figure") {
    override fun format() = Representations.setRepr(
		"figure",
		ArgumentEntry(false, "placement", placement),
		ArgumentEntry(false, "scope", scope),
		ArgumentEntry(false, "caption", caption),
		ArgumentEntry(false, "kind", kind),
		ArgumentEntry(false, "supplement", supplement),
		ArgumentEntry(false, "numbering", numbering),
		ArgumentEntry(false, "gap", gap),
		ArgumentEntry(false, "outlined", outlined),
	)
}

