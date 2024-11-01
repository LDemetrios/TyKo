package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TRegexSelector
(
	val regex: TRegex,
) : TSelector{
    override fun format() = Representations.reprOf(this)

    companion object  {
        internal val regexType : InternalType = ConcreteType("regex")

    }
}
