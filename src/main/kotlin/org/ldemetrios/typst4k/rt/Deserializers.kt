package org.ldemetrios.typst4k.rt

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.ldemetrios.js.*
import org.ldemetrios.typst4k.model.TValue
import org.ldemetrios.typst4k.model.deserialize
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castOrNull


inline fun <reified T : TValue> deserializeTypstValue(input: String): T {
    println("===========H========")
    println(input)
    println("=========E==========")
    val raw = JSParser.parseValue(input)
    println(raw.toString(4))
//    System.err.println(raw.toString(4))
//    System.err.println()
//    System.err.println(js.toString(4))
//    System.err.println()
    return deserialize<T>(raw) as T
}
