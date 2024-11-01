@file:OptIn(ExperimentalContracts::class)
@file:JvmName("ManualDeserialization")

package org.ldemetrios.typst4k.model

import kotlinx.serialization.SerializationException
import org.ldemetrios.js.*
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.*

class TypstSerialException : SerializationException {
    public constructor()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

@OptIn(ExperimentalContracts::class)
inline fun check(value: Boolean, message: () -> String) {
    contract {
        returns() implies value
    }
    if (!value) {
        throw TypstSerialException(message())
    }
}

fun failWith(message: String): Nothing = throw TypstSerialException(message)


internal fun maybeDeser(value: JSStuff, type: InternalType, path: String): TValue? {
    return if (value is JSUndefined) null else deserialize(value, type, path)
}

internal fun deserialize(value: JSStuff, type: InternalType, path: String = ""): TValue {
    println(path)
    return when (value) {
        is JSUndefined -> failWith("Missing value at path $path")
        is JSNull -> {
            type.optionOf("none")
                ?: failWith("None of `$type` can be represented with js null, only `none` can (at path $path)")
            TNone
        }

        is JSBoolean -> {
            type.optionOf("bool")
                ?: failWith("None of `$type` can be represented with js boolean, only `bool` can (at path $path)")
            TBool(value.toBoolean())
        }

        is JSNumber -> {
            val int = type.optionOf("int") != null
            val float = type.optionOf("float") != null
            when {
                int && float -> value.toString().toLongOrNull()?.let(::TInt) ?: TFloat(value.toDouble())
                int -> (value.toString().toLongOrNull()
                    ?: failWith("$value can't express `int`, only `float` (at path $path)"))
                    .let(::TInt)

                float -> TFloat(value.toDouble())
                else -> failWith("None of `$type` can be represented with js boolean, only `int` or `float` can (at path $path)")
            }
        }

        is JSArray -> {
            val option = type.optionOf("array")
                ?: failWith("None of `$type` can be represented with js array, only `array` can (at path $path)")
            val generic = option.params!![0]
            TArray(
                value.mapIndexed { ind, it ->
                    deserialize(it, generic, "$path/$ind")
                }
            )
        }

        is JSString -> {
            val enums = when (type) {
                is AnyType -> listOf("str")
                is ConcreteType -> listOf(type.ident)
                is UnionType -> type.options
                    .map { it.ident }.toSet()
                    .intersect(enumTypes.keys)
                    .let { if (it.isEmpty()) listOf("str") else it }

                else -> throw AssertionError()
            }.toList()
            when (enums.size) {
                0 -> failWith("None of `$type` can be represented with js string, only `str` or enumerated types can (at path $path)")
                1 -> if (enums[0] == "str") TStrImpl(value.str) else enumTypes[enums[0]]!!(value.str) ?: failWith(
                    "`${enums[0]}` (which is specified as option in `$type`) has no `${value.str}` variant (at path $path)"
                )

                else -> failWith("Multiple variants of `$type` can be represented with js string: `str` and enumerated types can (at path $path)")
            }
        }

        is JSObject -> {
            val desc = inferDescriptor(value, path)
            if (desc != null) {
                val handler = deserializers[desc] ?: deserializers["math.$desc"]
                if (handler != null) {
                    handler(value, type, path)
                } else {
                    val option = type.optionOf("dictionary")
                        ?: failWith("Invalid type descriptor `$desc` is present, and none of `$type` is a dictionary options (at path $path)")
                    val generic = option.params!![0]
                    TDictionary(
                        value.mapValues {
                            deserialize(it.value, generic, "$path/${it.key}")
                        }
                    )
                } // /0/value/children/2/child/children/0/children
            } else {
                val synthetics = when (type) {
                    is AnyType -> listOf()
                    is ConcreteType -> listOf(type.ident)
                    is UnionType -> type.options
                        .map { it.ident }.toSet()


                    else -> throw AssertionError()
                }.intersect(syntheticTypes.keys)
                    .let { if (it.isEmpty()) listOf("dictionary") else it }
                    .toList()

                when (synthetics.size) {
                    0 -> failWith("None of `$type` can be represented with js object, only `dictionary` or synthetic types can (at path $path)")
                    1 -> if (synthetics[0] == "dictionary") {
                        val option = type.optionOf("dictionary")!!
                        val generic = option.params!![0]
                        TDictionary(
                            value.mapValues {
                                deserialize(it.value, generic, "$path/${it.key}")
                            }
                        )
                    } else {
                        val deserializer = syntheticTypes[synthetics[0]]
                            ?: failWith("No object deserializer for type `${synthetics[0]}` found (at path $path)")
                        deserializer(value, type.optionOf(synthetics[0])!!, path)
                    }

                    else -> failWith("Multiple variants of `$type` can be represented with js object: `dictionary` and synthetic types can (at path $path)")
                }
            }
        }

        else -> throw AssertionError()
    }
}

private fun inferDescriptor(value: JSStuff, path: String): String? {
    val typeDesc = value["type"].let {
        when (it) {
            is JSUndefined -> null
            is JSString -> it.str
            else -> failWith("Type descriptor should either be missing or be a string, not ${it.javaClass.simpleName} (at path $path)")
        }
    }
    val funcDesc =
        if (typeDesc == "context" || typeDesc == "style" || typeDesc == "layout" || typeDesc == "locate") null else value["func"].let {
            when (it) {
                is JSUndefined -> null
                is JSString -> it.str
                else -> failWith("Subtype descriptor should either be missing or be a string, not ${it.javaClass.simpleName} (at path $path)")
            }
        }

    val desc = when ((typeDesc != null) to (funcDesc != null)) {
        true to true -> {
            when (typeDesc) {
                "func" -> throw SerializationException("Dynamic functions aren't supported")
                "gradient" -> "gradient." + funcDesc!!
//                        "content" -> when (funcDesc!!) {
////                            "vline", "hline", "header", "footer", "cell" -> "grid.$funcDesc"
//                            "caption" -> "figure.caption"
//                            "flush" -> "place.flush"
//                            else -> funcDesc
//                        }
                "selector" -> "$funcDesc-selector"
                "counter-key" -> "$funcDesc-counter-key"

                "color" -> when (funcDesc) {
                    "linear-rgb", "hsl", "hsv" -> "color.$funcDesc"
                    else -> funcDesc!!
                }

                "transform", "style", "property" -> typeDesc // TODO revisit after proper work on Typst side
                else -> throw AssertionError("$typeDesc . $funcDesc")
            }
        }

//            true to false -> res["type"] = type!!.js
        true to false -> when (typeDesc) {
//            "equation" -> "math.equation"
            "counter-update" -> "counter.update"
            "state-update" -> "state.update"
            "set-rule" -> "set-" + value["elem"].cast<JSString>().str
            "style" -> "style-deprecated"
            "layout" -> "layout"
            "relative" -> "relative-impl"
            "func" -> throw SerializationException("Dynamic functions aren't supported")
            else -> typeDesc!!
        }

        false to true -> {
//                throw AssertionError("null . $func")
            funcDesc!!
        }

        else -> null
    }
    return desc
}

private val strType = ConcreteType("str")

internal fun deserializeElement(value: JSStuff, type: InternalType, path: String = ""): TValue {
    return TElement(deserialize(value["name"], strType, "$path/name") as TStr)
}

fun camelCaseToKebabCase(input: String): String {
    // Use a regular expression to find positions where a lowercase letter is followed by an uppercase letter
    // Then replace those occurrences with the lowercase letter and a hyphen followed by the lowercase of the uppercase letter
    return input.replace(Regex("(?<=[a-z])(?=[A-Z])"), "-") // insert hyphen before each uppercase letter
        .replace(
            Regex("(?<=[A-Z])(?=[A-Z][a-z])"),
            "-"
        ) // insert hyphen between consecutive uppercase followed by a lowercase letter
        .lowercase() // Convert the whole string to lowercase
}


internal fun convert(type: KTypeProjection): InternalType = when (type.variance) {
    null -> convert(type.type!!)
    KVariance.INVARIANT -> convert(type.type!!)
    KVariance.IN -> convert<TValue>()
    KVariance.OUT -> convert(type.type!!)
}!!

internal fun convert(type: KType): InternalType? {
    val classifier = type.classifier ?: failWith("Can't deserialize type $type with `null` classifier")
    check(classifier is KClass<*>) { "Can't deserialize type $type non-class `$classifier` classifier" }
    if (classifier == TValue::class) return AnyType
    val union = classifier.annotations.filterIsInstance<UnionSyntheticType>()
    return when (union.size) {
        0 -> {
            val name = classifier.simpleName
                ?: failWith("Can't deserialize type $type with classifier `$classifier` without name")
            val clazz = camelCaseToKebabCase(name.drop(1))
            ConcreteType(
                clazz,
                type.arguments.map(::convert)
            )
        }

        1 -> {
            val variants = union[0].options
            val genericParamNumbers = variants.map { genericTypes[it] ?: 0 }
            val genericsPrefSum = genericParamNumbers.fold(mutableListOf(0)) { acc, number ->
                acc.apply { add(last() + number) }
            }
            val actualGenerics = type.arguments.map(::convert)
            UnionType(
                variants.mapIndexed { ind, variant ->
                    ConcreteType(
                        variant,
                        (genericsPrefSum[ind] until genericsPrefSum[ind + 1]).map {
                            actualGenerics[it]
                        }
                    )
                }
            )
        }

        else -> throw AssertionError("Multiple `UnionSyntheticType` synthetic types")
    }
}

internal inline fun <reified T : TValue> convert() = convert(typeOf<T>())

fun deserialize(value: JSStuff, type: KType): TValue = deserialize(value, convert(type)!!)

inline fun <reified T : TValue> deserialize(value: JSStuff): TValue = deserialize(value, typeOf<T>())

//fun main() {
//    println(convert<TValue>())
//    println(convert<TContent>())
//    println(convert<TAutoOrBoolOrFunctionOrNoneOrRelative>())
//    println(convert<TAuto>())
//    println(convert<TDictionary<TStr>>())
//    println(convert<TAutoOrDictionaryOrRelative<TDictionary<TArray<TArguments<TInt>>>>>())
//}


internal fun deserializeCounterUpdate(value: JSStuff, type: InternalType, path: String): TValue {
    val option = type.optionOf("counter.update")
        ?: type.optionOf("content")
    check(option != null) { "`counter.update`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
    val key: TFunctionOrLabelOrLocationOrSelectorOrStr =
        deserialize(value["key"], TCounterUpdate.keyType, "$path/key").castUnchecked()
    val update: TArrayOrFunctionOrInt<TInt> =
        deserialize(value["update"], TCounterUpdate.updateType, "$path/update").castUnchecked()
    // Although int can be passed to counter.update (therefore it is variant of TCounterUpdate.updateType),
    // it can't be serialized that way. If there's int, it's actually step. Kill me plz.
    return if (update is TInt) TCounterStep(key, update)
    else TCounterUpdate(key, update)
}

internal fun deserializeCounterStep(value: JSStuff, type: InternalType, path: String): TValue {
    throw AssertionError("Unreachable")
}