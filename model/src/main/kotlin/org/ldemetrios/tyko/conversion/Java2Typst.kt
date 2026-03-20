package org.ldemetrios.tyko.conversion

import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.TArray
import org.ldemetrios.tyko.model.TBool
import org.ldemetrios.tyko.model.TBytes
import org.ldemetrios.tyko.model.TDecimal
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TFloat
import org.ldemetrios.tyko.model.TInt
import org.ldemetrios.tyko.model.TJvmObject
import org.ldemetrios.tyko.model.TNone
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TValue
import java.math.BigDecimal
import java.util.stream.Stream

fun java2typst(value: Any?): TValue {
    return when (value) {
        null -> TNone
        is IntoValue -> value.intoValue()
        is Boolean -> TBool(value)
        is Byte -> TInt(value.toLong())
        is Short -> TInt(value.toLong())
        is Int -> TInt(value.toLong())
        is Long -> TInt(value)
        is Float -> TFloat(value.toDouble())
        is Double -> TFloat(value)
        is Char -> TStr(value.toString())
        is String -> TStr(value)
        is BigDecimal -> TDecimal(value.toPlainString())
        is ByteArray -> TBytes(value)
        is Pair<*, *> -> TArray(listOf(java2typst(value.first), java2typst(value.second)))
        is Map.Entry<*, *> -> TArray(listOf(java2typst(value.key), java2typst(value.value)))
        is Triple<*, *, *> -> TArray(listOf(java2typst(value.first), java2typst(value.second), java2typst(value.third)))

        else -> TJvmObject(value)
    }
}