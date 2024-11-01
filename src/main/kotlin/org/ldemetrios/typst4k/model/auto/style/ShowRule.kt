package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TShowRule(
	val selector: TNoneOrSelector,
	val transform: TArrayOrContentOrFunction<TStyle, >,
) : TStyle {
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TShowRule
    companion object : TType("show-rule") {
        internal val selectorType : InternalType = UnionType(ConcreteType("none"), ConcreteType("selector"))
        internal val transformType : InternalType = UnionType(ConcreteType("array", listOf(ConcreteType("style"))), ConcreteType("content"), ConcreteType("function"))

    }
}
