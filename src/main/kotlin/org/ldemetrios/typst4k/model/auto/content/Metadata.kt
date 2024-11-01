package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TMetadata<out D : TValue>(
	val value: D,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"metadata",
		ArgumentEntry(false, null, value),
	)
	override fun func() = TMetadata
    companion object : TElement("metadata") {
        internal fun valueType(D: InternalType) : InternalType = D

    }
}

