package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TAlignment(
	val horizontal: TStr? = null,
	val vertical: TStr? = null,
) : TValue, 
    TAlignmentOrAutoOrNone, 
    TAlignmentOrAuto, 
    TAlignmentOrArrayOrAutoOrFunction<Nothing> {
    override fun format() = Representations.reprOf(this)
	override fun type(): TType = TAlignment
    companion object : TType("alignment") {
        internal val horizontalType : InternalType = ConcreteType("str")
        internal val verticalType : InternalType = ConcreteType("str")

    }
}
