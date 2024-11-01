package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TStyled(
	val styles: TArray<TStyle, >,
	val child: TContent,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.reprOf(this)
	override fun func() = TStyled
    companion object : TElement("styled") {
        internal val stylesType : InternalType = ConcreteType("array", listOf(ConcreteType("style")))
        internal val childType : InternalType = ConcreteType("content")

    }
}

