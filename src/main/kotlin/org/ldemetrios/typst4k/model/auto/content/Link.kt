package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TLink(
	val dest: TLabelOrLocationOrPositionOrStr,
	val body: TContent,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"link",
		ArgumentEntry(false, null, dest),
		ArgumentEntry(false, null, body),
	)
	override fun func() = TLink
    companion object : TElement("link") {
        internal val destType : InternalType = UnionType(ConcreteType("label"), ConcreteType("location"), ConcreteType("position"), ConcreteType("str"))
        internal val bodyType : InternalType = ConcreteType("content")

    }
}

