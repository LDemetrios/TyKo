package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TStack(
	val children: TArray<TContentOrFractionOrRelative>,
	val dir: TDirection? = null,
	val spacing: TFractionOrNoneOrRelative? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"stack",
		ArgumentEntry(true, null, children),
		ArgumentEntry(false, "dir", dir),
		ArgumentEntry(false, "spacing", spacing),
	)
	override fun func() = TStack
    companion object : TElement("stack") {
        internal val childrenType : InternalType = ConcreteType("array", listOf(UnionType(ConcreteType("content"), ConcreteType("fraction"), ConcreteType("relative"))))
        internal val dirType : InternalType = ConcreteType("direction")
        internal val spacingType : InternalType = UnionType(ConcreteType("fraction"), ConcreteType("none"), ConcreteType("relative"))

    }
}

data class TSetStack(
	val dir: TDirection? = null,
	val spacing: TFractionOrNoneOrRelative? = null,
) : TSetRule("stack") {
    override fun format() = Representations.setRepr(
		"stack",
		ArgumentEntry(false, "dir", dir),
		ArgumentEntry(false, "spacing", spacing),
	)
}

