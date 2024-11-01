package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TDuration(
	val seconds: TInt? = null,
	val minutes: TInt? = null,
	val hours: TInt? = null,
	val days: TInt? = null,
	val weeks: TInt? = null,
) : TValue {
    override fun format() = Representations.structRepr(
		"duration",
		ArgumentEntry(false, "seconds", seconds),
		ArgumentEntry(false, "minutes", minutes),
		ArgumentEntry(false, "hours", hours),
		ArgumentEntry(false, "days", days),
		ArgumentEntry(false, "weeks", weeks),
	)
	override fun type(): TType = TDuration
    companion object : TType("duration") {
        internal val secondsType : InternalType = ConcreteType("int")
        internal val minutesType : InternalType = ConcreteType("int")
        internal val hoursType : InternalType = ConcreteType("int")
        internal val daysType : InternalType = ConcreteType("int")
        internal val weeksType : InternalType = ConcreteType("int")

    }
}
