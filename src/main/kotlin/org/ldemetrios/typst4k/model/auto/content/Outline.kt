package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TOutline(
	val title: TAutoOrContentOrNone? = null,
	val target: TFunctionOrLabelOrLocationOrSelector? = null,
	val depth: TIntOrNone? = null,
	val indent: TAutoOrBoolOrFunctionOrNoneOrRelative? = null,
	val fill: TContentOrNone? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"outline",
		ArgumentEntry(false, "title", title),
		ArgumentEntry(false, "target", target),
		ArgumentEntry(false, "depth", depth),
		ArgumentEntry(false, "indent", indent),
		ArgumentEntry(false, "fill", fill),
	)
	override fun func() = TOutline
    companion object : TElement("outline") {
        internal val titleType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("content"), ConcreteType("none"))
        internal val targetType : InternalType = UnionType(ConcreteType("function"), ConcreteType("label"), ConcreteType("location"), ConcreteType("selector"))
        internal val depthType : InternalType = UnionType(ConcreteType("int"), ConcreteType("none"))
        internal val indentType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("bool"), ConcreteType("function"), ConcreteType("none"), ConcreteType("relative"))
        internal val fillType : InternalType = UnionType(ConcreteType("content"), ConcreteType("none"))

    }
}

data class TSetOutline(
	val title: TAutoOrContentOrNone? = null,
	val target: TFunctionOrLabelOrLocationOrSelector? = null,
	val depth: TIntOrNone? = null,
	val indent: TAutoOrBoolOrFunctionOrNoneOrRelative? = null,
	val fill: TContentOrNone? = null,
) : TSetRule("outline") {
    override fun format() = Representations.setRepr(
		"outline",
		ArgumentEntry(false, "title", title),
		ArgumentEntry(false, "target", target),
		ArgumentEntry(false, "depth", depth),
		ArgumentEntry(false, "indent", indent),
		ArgumentEntry(false, "fill", fill),
	)
}

