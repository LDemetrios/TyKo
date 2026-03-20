package org.ldemetrios.tyko.conversion

import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.TAngle
import org.ldemetrios.tyko.model.TArray
import org.ldemetrios.tyko.model.TBool
import org.ldemetrios.tyko.model.TBytes
import org.ldemetrios.tyko.model.TCmykColor
import org.ldemetrios.tyko.model.TDatetime
import org.ldemetrios.tyko.model.TDecimal
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TFloat
import org.ldemetrios.tyko.model.TFraction
import org.ldemetrios.tyko.model.TFunc
import org.ldemetrios.tyko.model.THslColor
import org.ldemetrios.tyko.model.THsvColor
import org.ldemetrios.tyko.model.TInt
import org.ldemetrios.tyko.model.TJvmObject
import org.ldemetrios.tyko.model.TLinearRgbColor
import org.ldemetrios.tyko.model.TLumaColor
import org.ldemetrios.tyko.model.TNone
import org.ldemetrios.tyko.model.TOklabColor
import org.ldemetrios.tyko.model.TOklchColor
import org.ldemetrios.tyko.model.TPath
import org.ldemetrios.tyko.model.TRatio
import org.ldemetrios.tyko.model.TRegex
import org.ldemetrios.tyko.model.TRgbColor
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TValue
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.file.Path
import java.lang.Boolean as JBoolean
import java.lang.Byte as JByte
import java.lang.Short as JShort
import java.lang.Character as JChar
import java.lang.Integer as JInt
import java.lang.Long as JLong
import java.lang.Float as JFloat
import java.lang.Double as JDouble

import java.lang.reflect.Array as JavaArray

private fun TValue.toNumber(): Number? = when (this) {
    is TInt -> value
    is TFloat -> value
    is TDecimal -> value.toBigDecimal()
    else -> null
}

private inline fun <reified T> Any.cast() = this as? T

fun typst2java(value: TValue, expectedType: Class<*>): Any? {
    if (IntoValue::class.java.isAssignableFrom(expectedType)) return value

    if (value is TJvmObject) return value.value
    if (value is TNone) return null

    val result = when (expectedType) {
        JBoolean.TYPE, JBoolean::class.java -> value.cast<TBool>()?.value
        JByte.TYPE, JByte::class.java -> value.toNumber()?.toByte()
        JShort.TYPE, JShort::class.java -> value.toNumber()?.toShort()
        JInt.TYPE, JInt::class.java -> value.toNumber()?.toInt()
        JLong.TYPE, JLong::class.java -> value.toNumber()?.toLong()
        JFloat.TYPE, JFloat::class.java -> value.toNumber()?.toFloat()
        JDouble.TYPE, JDouble::class.java -> value.toNumber()?.toDouble()
        BigInteger::class.java -> when (value) {
            is TInt -> value.value.toBigInteger()
            is TFloat -> value.value.toBigDecimal().toBigInteger()
            is TDecimal -> value.value.toBigDecimal().toBigInteger()
            else -> null
        }

        BigDecimal::class.java -> when (value) {
            is TInt -> value.value.toBigDecimal()
            is TFloat -> value.value.toBigDecimal()
            is TDecimal -> value.value.toBigDecimal()
            else -> null
        }

        JChar.TYPE, JChar::class.java -> value.toNumber()?.toInt()?.toChar()
            ?: value.cast<TStr>()?.value?.takeIf { it.length == 1 }?.get(0)

        String::class.java, CharSequence::class.java -> value.cast<TStr>()?.value

        Any::class.java -> when (value) {
            is TBool -> value.value
            is TInt -> value.value
            is TFloat -> value.value
            is TStr -> value.value
            is TDecimal -> value.value.toBigDecimal()
            is TArray<*> -> value.value.map { it.intoValue() }
            is TDict<*> -> value.value.mapValues { (_, inner) -> typst2java(inner.intoValue(), Any::class.java) }
            is TBytes -> value.value
            is TPath -> Path.of(value.path)
            is TRegex -> Regex(value.regex.value)
            is TFunc -> null // TODO ???
            is TAngle -> value.value.value
            is TDatetime -> TODO()
            is TCmykColor -> TODO() // java.awt.Color
            is THslColor -> TODO() // java.awt.Color
            is THsvColor -> TODO() // java.awt.Color
            is TLinearRgbColor -> TODO() // java.awt.Color
            is TLumaColor -> TODO() // java.awt.Color
            is TOklabColor -> TODO() // java.awt.Color
            is TOklchColor -> TODO() // java.awt.Color
            is TRgbColor -> TODO() // java.awt.Color
            is TRatio -> value.value
            is TFraction -> value.value
            else -> null
        }

        Map::class.java -> {
            value.cast<TDict<*>>()?.value?.mapValues { (_, inner) ->
                typst2java(
                    inner.intoValue(),
                    Any::class.java
                )
            }
        }

        List::class.java -> when (value) {
            is TBytes -> value.value.toList()
            is TStr -> value.value.toList()
            is TArray<*> -> value
            is TDict<*> -> value.map { (k, v) -> k to typst2java(v.intoValue(), Any::class.java) }
            else -> null
        }

        Set::class.java -> when (value) {
            is TStr -> value.value.toSet()
            is TArray<*> -> value.toSet()
            else -> null
        }

        else -> if (expectedType.isArray) when (expectedType) {
            ByteArray::class.java if value is TBytes -> value.value
            CharArray::class.java if value is TStr -> value.value.toCharArray()
            else -> value
                .cast<TArray<*>>()
                ?.map { typst2java(it.intoValue(), expectedType.componentType) }
                ?.let { arrayFrom(expectedType, it) }
        } else null
    }
    return result ?: error("Can't convert ${value.type().name.value} into ${expectedType.canonicalName}")
}

private fun arrayFrom(expectedType: Class<*>, list: List<Any?>): Any = when (expectedType.componentType) {
    JBoolean.TYPE -> list.map { it as Boolean }.toBooleanArray()
    JByte.TYPE -> list.map { it as Byte }.toByteArray()
    JShort.TYPE -> list.map { it as Short }.toShortArray()
    JInt.TYPE -> list.map { it as Int }.toIntArray()
    JLong.TYPE -> list.map { it as Long }.toLongArray()
    JFloat.TYPE -> list.map { it as Float }.toFloatArray()
    JDouble.TYPE -> list.map { it as Double }.toDoubleArray()
    JChar.TYPE -> list.map { it as Char }.toCharArray()
    else -> {
        val result = JavaArray.newInstance(expectedType.componentType, list.size)
        list.forEachIndexed { idx, value ->
            JavaArray.set(result, idx, value)
        }
        result
    }
}

fun props(): Int = TODO()