package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

interface TModule : TValue {
	abstract val name: TStr
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TModule
    companion object : TType("module") {
        internal val nameType : InternalType = ConcreteType("str")

    }
}
