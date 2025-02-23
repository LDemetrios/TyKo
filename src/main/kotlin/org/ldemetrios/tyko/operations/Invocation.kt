package org.ldemetrios.tyko.operations

import org.ldemetrios.tyko.model.*
import org.ldemetrios.tyko.model.repr
import java.util.*

fun TFunction.with(arguments: TArguments<*>) = TWith(this, arguments)

fun TFunction.with(vararg positionals: TValue) = with(TArguments(TArray(*positionals), TDictionary<TValue>()))
fun TFunction.with(vararg named: Pair<String, TValue>) = with(TArguments(TArray(), TDictionary(*named)))

operator fun TFunction.get(vararg named: Pair<String, TValue>) = with(*named)

operator fun TFunction.invoke(vararg args: TValue): TValue {
//    val uuid = UUID.randomUUID().toString()
//    val text = "#metadata((${this.repr()})(${args.joinToString { it.repr() }}))<$uuid>"
//    val result = TypstDefaultCompiler.compiler
//        .query<TMetadata<TValue>>(text, TLabel(uuid.t))
//        .orElseThrow()
//    if (result.size != 1) throw AssertionError("Found ${result.size} <$uuid> labels, should've found 1")
//    return result[0].value
    return TODO()
}
