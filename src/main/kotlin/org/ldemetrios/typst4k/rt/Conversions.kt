package org.ldemetrios.typst4k.rt

import org.ldemetrios.typst4k.model.*
import org.ldemetrios.typst4k.model.TDictionaryImpl
import java.awt.Color

val Float.t get() = TFloat(this.toDouble())
val Double.t get() = TFloat(this)
val Byte.t get() = TInt(this.toLong())
val Short.t get() = TInt(this.toLong())
val Int.t get() = TInt(this.toLong())
val Long.t get() = TInt(this)
val String.t get() = TStr(this)
val Boolean.t get() = TBool(this)

val TFloat.pt get() = TLength(this, 0f.t)
val TFloat.em get() = TLength(0f.t, this)
val TInt.pt get() = TLength(value.toDouble().t, 0f.t)
val TInt.em get() = TLength(0f.t, value.toDouble().t)

val TFloat.fr get() = TFraction(this)
val TInt.fr get() = TFraction(value.toDouble().t)

val TFloat.pc get() = TRatio((value / 100).t)
val TInt.pc get() = TRatio((value / 100.0).t)

val TFloat.deg get() = TAngle(this)
val TInt.deg get() = TAngle(value.toDouble().t)
val TFloat.rad get() = TAngle((this.value * (180 / Math.PI)).t)
val TInt.rad get() = TAngle(((this.value * (180 / Math.PI))).t)

fun <E : TValue> TArray(vararg elements: E) = TArray(elements.toList())
fun <E : TValue> TDictionary(vararg pairs: Pair<String, E>) = TDictionaryImpl(pairs.toMap())
fun TSequence(vararg elements: TContent) = TSequence(TArray(elements.toList()))

// Temporary, later replace with code generation
val Byte.pt get() = this.t.pt
val Byte.em get() = this.t.em
val Byte.fr get() = this.t.fr
val Byte.pc get() = this.t.pc
val Byte.deg get() = this.t.deg
val Byte.rad get() = this.t.rad

val Short.pt get() = this.t.pt
val Short.em get() = this.t.em
val Short.fr get() = this.t.fr
val Short.pc get() = this.t.pc
val Short.deg get() = this.t.deg
val Short.rad get() = this.t.rad

val Int.pt get() = this.t.pt
val Int.em get() = this.t.em
val Int.fr get() = this.t.fr
val Int.pc get() = this.t.pc
val Int.deg get() = this.t.deg
val Int.rad get() = this.t.rad

val Long.pt get() = this.t.pt
val Long.em get() = this.t.em
val Long.fr get() = this.t.fr
val Long.pc get() = this.t.pc
val Long.deg get() = this.t.deg
val Long.rad get() = this.t.rad

val Float.pt get() = this.t.pt
val Float.em get() = this.t.em
val Float.fr get() = this.t.fr
val Float.pc get() = this.t.pc
val Float.deg get() = this.t.deg
val Float.rad get() = this.t.rad

val Double.pt get() = this.t.pt
val Double.em get() = this.t.em
val Double.fr get() = this.t.fr
val Double.pc get() = this.t.pc
val Double.deg get() = this.t.deg
val Double.rad get() = this.t.rad

val Color.t get() = TRgb("#%02x%02x%02x%02x".format(red, green, blue, alpha).t)
