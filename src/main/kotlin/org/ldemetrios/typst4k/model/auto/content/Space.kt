package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TSpace(
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.reprOf(this)
	override fun func() = TSpace
    companion object : TElement("space") {

    }
}

