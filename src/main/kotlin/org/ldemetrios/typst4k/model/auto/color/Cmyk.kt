package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TCmyk
(
	val cyan: TRatio,
	val magenta: TRatio,
	val yellow: TRatio,
	val key: TRatio,
) : TColor{
    override fun format() = Representations.structRepr(
        "cmyk",
        ArgumentEntry(false, null, cyan),
        ArgumentEntry(false, null, magenta),
        ArgumentEntry(false, null, yellow),
        ArgumentEntry(false, null, key),
    )

    companion object  {
        internal val cyanType : InternalType = ConcreteType("ratio")
        internal val magentaType : InternalType = ConcreteType("ratio")
        internal val yellowType : InternalType = ConcreteType("ratio")
        internal val keyType : InternalType = ConcreteType("ratio")

    }
}
