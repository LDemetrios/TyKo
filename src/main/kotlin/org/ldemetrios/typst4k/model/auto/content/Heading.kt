package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class THeading(
	val body: TContent,
	val level: TAutoOrInt? = null,
	val depth: TInt? = null,
	val offset: TInt? = null,
	val numbering: TFunctionOrNoneOrStr? = null,
	val supplement: TAutoOrContentOrFunctionOrNone? = null,
	val outlined: TBool? = null,
	val bookmarked: TAutoOrBool? = null,
	val hangingIndent: TAutoOrLength? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"heading",
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "level", level),
		ArgumentEntry(false, "depth", depth),
		ArgumentEntry(false, "offset", offset),
		ArgumentEntry(false, "numbering", numbering),
		ArgumentEntry(false, "supplement", supplement),
		ArgumentEntry(false, "outlined", outlined),
		ArgumentEntry(false, "bookmarked", bookmarked),
		ArgumentEntry(false, "hanging-indent", hangingIndent),
	)
	override fun func() = THeading
    companion object : TElement("heading") {
        internal val bodyType : InternalType = ConcreteType("content")
        internal val levelType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("int"))
        internal val depthType : InternalType = ConcreteType("int")
        internal val offsetType : InternalType = ConcreteType("int")
        internal val numberingType : InternalType = UnionType(ConcreteType("function"), ConcreteType("none"), ConcreteType("str"))
        internal val supplementType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("content"), ConcreteType("function"), ConcreteType("none"))
        internal val outlinedType : InternalType = ConcreteType("bool")
        internal val bookmarkedType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("bool"))
        internal val hangingIndentType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("length"))

    }
}

data class TSetHeading(
	val level: TAutoOrInt? = null,
	val depth: TInt? = null,
	val offset: TInt? = null,
	val numbering: TFunctionOrNoneOrStr? = null,
	val supplement: TAutoOrContentOrFunctionOrNone? = null,
	val outlined: TBool? = null,
	val bookmarked: TAutoOrBool? = null,
	val hangingIndent: TAutoOrLength? = null,
) : TSetRule("heading") {
    override fun format() = Representations.setRepr(
		"heading",
		ArgumentEntry(false, "level", level),
		ArgumentEntry(false, "depth", depth),
		ArgumentEntry(false, "offset", offset),
		ArgumentEntry(false, "numbering", numbering),
		ArgumentEntry(false, "supplement", supplement),
		ArgumentEntry(false, "outlined", outlined),
		ArgumentEntry(false, "bookmarked", bookmarked),
		ArgumentEntry(false, "hanging-indent", hangingIndent),
	)
}

