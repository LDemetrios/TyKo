package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TDatetime(
	val year: TInt? = null,
	val month: TInt? = null,
	val day: TInt? = null,
	val hour: TInt? = null,
	val minute: TInt? = null,
	val second: TInt? = null,
) : TValue, 
    TAutoOrDatetimeOrNone {
    override fun format() = Representations.structRepr(
		"datetime",
		ArgumentEntry(false, "year", year),
		ArgumentEntry(false, "month", month),
		ArgumentEntry(false, "day", day),
		ArgumentEntry(false, "hour", hour),
		ArgumentEntry(false, "minute", minute),
		ArgumentEntry(false, "second", second),
	)
	override fun type(): TType = TDatetime
    companion object : TType("datetime") {
        internal val yearType : InternalType = ConcreteType("int")
        internal val monthType : InternalType = ConcreteType("int")
        internal val dayType : InternalType = ConcreteType("int")
        internal val hourType : InternalType = ConcreteType("int")
        internal val minuteType : InternalType = ConcreteType("int")
        internal val secondType : InternalType = ConcreteType("int")

    }
}
