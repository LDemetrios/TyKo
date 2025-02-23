@file:OptIn(ExperimentalContracts::class)
@file:JvmName("ManualDeserialization")

package org.ldemetrios.tyko.model

import kotlinx.serialization.SerializationException
import org.ldemetrios.js.*
import org.ldemetrios.tyko.operations.t
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castOrNull
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.primaryConstructor

internal val labelType = ConcreteType("label")

class TypstSerialException : SerializationException {
    public constructor()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

@OptIn(ExperimentalContracts::class)
inline fun check(value: Boolean, path: String, message: () -> String) {
    contract {
        returns() implies value
    }
    if (!value) failWith(message(), path)
}

fun failWith(message: String, path: String): Nothing = throw TypstSerialException("$message (at path `$path`)")

fun KType.inferClassifier(path: String) = when (val classifier = this.classifier) {
    is KClass<*> -> classifier
    null -> failWith("Type `$this` has null classifier", path)
    is KTypeParameter -> failWith(
        "TypeParameters are not supported as types for deserialization yet. Please specify concrete type.",
        path
    )

    else -> failWith(
        "Unknown classifier kind `$classifier` (instance of `${classifier.javaClass}`) for type `$this`",
        path
    )
}

@PublishedApi
internal fun deserialize(value: JSStuff, path: String = "", expected: Class<*> = TValue::class.java): TValue? = when (value) {
    is JSUndefined -> null
    is JSNull -> {
        if (expected.isAssignableFrom(TNone::class.java)) TNone else null
    }

    is JSBoolean -> {
        TBool(value.toBoolean())
    }

    is JSNumber -> when (value.number) {
        is BigInteger -> TInt(value.toLong())
        is BigDecimal -> TFloat(value.toDouble())
        else -> failWith("Unknown number type `$value`", path)
    }

    is JSArray -> {
        TArray(
            value.mapIndexed { ind, it -> deserialize(it, "$path/$ind") ?: failWith("Missing element $ind", path) }
        )
    }

    is JSString -> {
        THAlignment.of(value.str).takeIf { expected.isAssignableFrom(THAlignment::class.java) }
            ?: TVAlignment.of(value.str).takeIf { expected.isAssignableFrom(TVAlignment::class.java) }
            ?: TStr(value.str)
    }

    is JSObject -> {
        val desc = inferDescriptor(value, path)
        if (desc != null) {
            concreteType(desc, value, path)
        } else {
            dictionaryType(value, path)
        }
    }

    else -> throw AssertionError()
}

private fun dictionaryType(value: JSObject, path: String): TValue? {
    return TDictionary<TValue>(
        value.mapValues { (k, v) ->
            deserialize(v, "$path/$k") ?: failWith("Missing element `$k`", path)
        }
    ) as TValue?

}

private fun concreteType(desc: String, value: JSObject, path: String): TValue {
    return when (desc) {
        "element" -> {
            val name = value["name"].castOrNull<JSString>()?.str ?: failWith("Missing `name` field, or not a js string", path)
            TElementImpl(name)
        }

        "type" -> {
            val name = value["name"].castOrNull<JSString>()?.str ?: failWith("Missing `name` field, or not a js string", path)
            TTypeImpl(name)
        }

        "none" -> TNone

        "counter.update" -> {
//            public val key: TFunctionOrLabelOrLocationOrSelectorOrStr
//
//            public val update: TArrayOrFunctionOrInt<TInt>
            val key = deserialize(value["key"], "$path/key", TFunctionOrLabelOrLocationOrSelectorOrStr::class.java) ?: failWith("Missing `key` field", path)
            val update = deserialize(value["update"], "$path/update", TFunctionOrLabelOrLocationOrSelectorOrStr::class.java) ?: failWith("Missing `key` field", path)

            if (update is TInt) {
                TCounterStep(key.castOrNull() ?: failWith("Can't cast key of type ${key.javaClass}", path), update)
            } else {
                TCounterUpdate(
                    key.castOrNull() ?: failWith("Can't cast key of type ${key.javaClass}", path),
                    update.castOrNull() ?: failWith("Can't cast update of type ${update.javaClass}", path)
                )
            }
        }

        "bytes" -> {
            val arr = value["value"].castOrNull<JSArray>()?.array ?: failWith("`value` of bytes should be an array of ints", path)
            val bytes = arr.mapIndexed { ind, it -> it.castOrNull<JSNumber>()?.number?.toByte() ?: failWith("Not an int", "$path/$ind") }.toByteArray()
            TBytes(bytes)
        }

        "auto" -> TAuto
        "plugin" -> failWith("Plugin deserialization is not supported yet", path)
        "align-point" -> TMathAlignPoint(value["label"].takeIf { it != JSUndefined }?.let { deserialize(it) as? TLabel ?: failWith("Incorrect label type ${it.javaClass}", path) })

        "symbol" -> {
            when (val text = value["text"]) {
                is JSUndefined -> {
                    // it's type
                    val variants = deserialize(value["variants"]) as? TArray<*> ?: failWith("Symbol should contain either string `text` or array `variants`", path)
                    TSymbol(variants as TArray<TArrayOrStr<TStr>>)
                }

                is JSString -> {
                    TSymbolElem(text.str.t)
                }

                else -> failWith("`text` in symbol should be either js string or missing", path)
            }
        }

        else -> if (desc.startsWith("set-")) {
            val id = value["id"].castOrNull<JSString>()?.str ?: failWith("Missing `id` for set math.equation", path)
            val toSet = value["value"].takeIf { it !is JSUndefined } ?: failWith("Missing `value` for set math.equation", path)
            when (desc) {
                "set-math.equation" -> {
                    when (id) {
                        "variant" -> {
                            val variantStr = toSet.castOrNull<JSString>()?.str ?: failWith("math.equation variant expected to be string", path)
                            TShowRule(TNone, TNativeFunc("math.$variantStr".t))
                        }
                        "italic" -> {
                            val italic = toSet.castOrNull<JSBoolean>()?.toBoolean() ?: failWith("math.equation italic expected to be boolean", path)
                            if (italic) {
                                 failWith("Redundant set italic:true in math equation", path)
                                TShowRule(TNone, TNativeFunc("math.italic".t))
                            } else {
//                                 failWith("Redundant set italic:false in math equation", path)
                                TShowRule(TNone, TNativeFunc("math.upright".t))
                            }
                        }
                        "size" -> {
                            val size = toSet.castOrNull<JSString>()?.str ?: failWith("math.equation size expected to be boolean", path)
                            when (size) {
                                "script-script" -> TShowRule(TNone, TNativeFunc("math.sscript".t))
                                else -> failWith("Unknown math.equation size `$size`", path)
                            }
                        }
                        "bold" -> {
                            val bold = toSet.castOrNull<JSBoolean>()?.toBoolean() ?: failWith("math.equation bold expected to be boolean", path)
                            if (bold) {
                                TShowRule(TNone, TNativeFunc("math.bold".t))
                            } else {
                                 failWith("Redundant set bold:false in math equation", path)
//                                TShowRule(TNone, TClosure("it => it".t))
                            }
                        }
                        else -> {
                            concreteTypeReflect(desc, path, mapOf(id to toSet))
                        }
                    }
                }

                else -> concreteTypeReflect(desc, path, mapOf(id to toSet))
            }
        } else {
            concreteTypeReflect(desc, path, value)
        }
    }

    /*
        val classifier = type.inferClassifier(path)

        val actualClassifier = interfaceTypes[desc] ?: setInterfaceTypes[desc] ?: when (desc) {
            "element" -> TElement::class
            "type" -> TType::class
            else -> failWith(
                "Can't find interface type for `$desc` descriptor", path
            )
        }

        check(classifier.isSuperclassOf(actualClassifier), path) { "`$desc` is not subclass of `$type`" }

        val annotation = classifier.annotations
        val unionAnn = annotation.filterIsInstance<TUnionType>().singleOrNull()
        val interfaceAnn = annotation.filterIsInstance<TInterfaceType>().singleOrNull()

        val params = when {
            classifier.typeParameters.isEmpty() -> listOf()
            unionAnn != null -> {
                val option = unionAnn.optionOf(desc, type, path)
                check(option != null, path) { "$desc is not in the union [${unionAnn.options.joinToString(", ")}]" }
                option.map { it.toType() }
            }

            interfaceAnn != null -> {
                classifier.typeParameters.map { it.toType() }
            }
    //        classifier == TValue::class -> {
    //            val impl = interfaceTypes[desc] ?: setInterfaceTypes[desc] ?: failWith(
    //                "Can't find interface type for `$desc` descriptor", path
    //            )
    //            impl to impl.typeParameters.map { it.toType() }
    //        }
    //        classifier == TSetRule -> {
    //            if (classifier )
    //            check(desc.startsWith("set-"), path) { "" }
    //            val impl = setInterfaceTypes[desc] ?: failWith("Can't find set rule `$desc` impl", path)
    //            check(classifier.isSuperclassOf(impl), path) { "$type is not supertype of $impl" }
    //            impl to listOf()
    //        }
    //        union != null -> {
    //            val parameters = union.optionOf(desc, type, path)?.map { it.toType() } ?: failWith(
    //                "Descriptor `$desc` is not in the union [${union.options.joinToString(", ")}] represented by type `$type`",
    //                path
    //            )
    //            val clazz = interfaceTypes[desc] ?: setInterfaceTypes[desc] ?: failWith(
    //                "Can't find interface type for `$desc` descriptor", path
    //            )
    //            clazz to parameters
    //        }
    //        synthetic != null || enum != null -> failWith(
    //            "`$type` can't be represented with js object, as it is synthetic or enum",
    //            path
    //        )
    //        interf != null -> {
    //            if (desc.startsWith("set-")) {
    //                val impl = setInterfaceTypes[desc] ?: failWith("Can't find set rule `$desc` impl", path)
    //                check(classifier.isSuperclassOf(impl), path) { "$type is not supertype of $impl" }
    //                impl to listOf()
    //            } else {
    //                val clazz = interfaceTypes[desc] ?: failWith("Can't find interface type for `$desc` descriptor", path)
    //                val chain = clazz.annotations.filterIsInstance<TInterfaceType>().single().chain
    //                check(chain.contains(interf.name), path) { "${interf.name} can't represent with `$desc`" }
    //                clazz to classifier.typeParameters.map { it.toType() }
    //            }
    //        }
    //        set != null -> {
    //            check(desc == "set-" + set.owner, path) {
    //                "`set-${set.owner}` (represented with `$type`) expected, `$desc` found"
    //            }
    //            setInterfaceTypes[desc]!! to type.arguments.map { it.toType() }
    //        }
            else -> throw AssertionError("$desc, $type")
        }

        val impl =
            actualClassifier.annotations.filterIsInstance<TInterfaceType>().singleOrNull()?.impl
                ?: actualClassifier.annotations.filterIsInstance<TSetRuleType>().singleOrNull()?.impl
                ?: failWith("No TInterfaceType annotation for $actualClassifier", path)

        val fields = impl.primaryConstructor!!.parameters.associateWith {
            val itsName = it.annotations.filterIsInstance<SerialName>().singleOrNull()?.name ?: failWith("WTF", path)
            val itsType = it.type.substitute(impl.typeParameters.zip(params).toMap(), "$path/$itsName")
            deserialize(value[itsName], itsType, "$path/$itsName")
        }
        return impl.primaryConstructor!!.callBy(fields) as TValue*/
}

private fun concreteTypeReflect(desc: String, path: String, effective: Map<String, JSStuff>): TValue {
    val impl = implTypes[desc] ?: failWith("Can't find impl for `$desc` descriptor", path)
    val fields = impl.primaryConstructor!!.parameters.associateWith {
        val itsName = it.annotations.filterIsInstance<SerialName>().singleOrNull()?.name ?: failWith("WTF", path)
        deserialize(effective[itsName] ?: JSUndefined, "$path/$itsName", expected = it.type.classifier.castOrNull<KClass<*>>()?.java ?: TValue::class.java)
    }
    return try {
        impl.primaryConstructor!!.callBy(fields)
    } catch (e: RuntimeException) {
        throw TypstSerialException(
            "Failed to instantiate $desc (at path $path, impl $impl, fields \n" +
                    fields.entries.joinToString("\n") { "${it.key.name} = ${it.value?.repr()}" } +
                    "\n)", e)
    }
}

private fun KType.substitute(map: Map<KTypeParameter, KType>, path: String): KType {
    val classifier = this.classifier ?: failWith("Null classifier of `$this`", path)
    val nullable = this.isMarkedNullable
    if (classifier is KTypeParameter && classifier in map) {
        val substituted = map[classifier]!!
        return substituted.classifier!!.createType(substituted.arguments, nullable || substituted.isMarkedNullable)
    }
    val substitutedArguments = this.arguments.mapIndexed { index, it ->
        when (it.variance) {
            null -> typeOf<TValue>()
            KVariance.INVARIANT -> it.type ?: typeOf<TValue>()
            KVariance.IN -> typeOf<TValue>()
            KVariance.OUT -> it.type ?: typeOf<TValue>()
        }.substitute(map, "$path::substituting/$index")
    }
    return classifier.createType(substitutedArguments.map { KTypeProjection.invariant(it) }, nullable)
}

private fun TUnionType.optionOf(desc: String, type: KType, path: String): List<KTypeProjection>? {
    val supertypes = interfaceTypes[desc]?.let {
        it.annotations.filterIsInstance<TInterfaceType>().singleOrNull()?.chain?.toList()
    } ?: setInterfaceTypes[desc]?.let {
        listOf("set-" + it.annotations.filterIsInstance<TSetRuleType>().single().owner, "style", "content")
    } ?: when (desc) {
        "sides" -> listOf("sides", "margin")
        // smth more?
        else -> listOf(desc)
    }// failWith("Can't find interface type for `$desc` descriptor", path)
    val descriptor = options.find { it in supertypes } ?: return null
    val before = options.takeWhile { it != descriptor }.sumOf { genericTypes[it]!! }
    return type.arguments.subList(before, before + genericTypes[descriptor]!!)
}

fun inferArrayElementType(type: KType, path: String): KType? {
    val classifier = type.inferClassifier(path)
    val annotation = classifier.annotations
    val union = annotation.filterIsInstance<TUnionType>().singleOrNull()
    return when {
        union != null -> {
            union.optionOf("array", type, path)?.single()?.toType() ?: failWith(
                "Can't find array element type in union `$type`", path
            )
        }

        classifier == TArray::class -> type.arguments[0].toType()
        classifier == TValue::class -> typeOf<TValue>()
        else -> null
    }
}

internal fun KTypeProjection.toType() = when (variance) {
    null -> typeOf<TValue>()
    KVariance.INVARIANT -> type ?: typeOf<TValue>()
    KVariance.IN -> typeOf<TValue>()
    KVariance.OUT -> type ?: typeOf<TValue>()
}

internal fun KTypeParameter.toType() = when (variance) {
    KVariance.INVARIANT -> upperBounds.singleOrNull() ?: typeOf<TValue>()
    KVariance.IN -> typeOf<TValue>()
    KVariance.OUT -> upperBounds.singleOrNull() ?: typeOf<TValue>()
}

internal fun KType.containsUnparameterized(opt: String, path: String): Boolean {
    val classifier = this.inferClassifier(path)
    val annotation = classifier.annotations
    val union = annotation.filterIsInstance<TUnionType>().singleOrNull()
    val synthetic = annotation.filterIsInstance<TSyntheticType>().singleOrNull()
    val enum = annotation.filterIsInstance<TEnumType>().singleOrNull()
    val set = annotation.filterIsInstance<TSetRuleType>().singleOrNull()
    val interf = annotation.filterIsInstance<TInterfaceType>().singleOrNull()

    return when {
        union != null -> union.options.contains(opt)
        synthetic != null -> synthetic.options.contains(opt) || opt == "dictionary"
        enum != null -> enum.options.contains(opt)
        set != null -> opt.startsWith("set-") && set.owner == opt.drop(4)
        interf != null -> interf.chain.contains(opt)
        else -> when (classifier) {
            TValue::class -> true
            TSetRule::class -> opt.startsWith("set-")
            else -> failWith("Invalid classifier `$classifier` inside `KType.containsUnparameterized`", path)
        }
    }
}

private fun inferDescriptor(value: JSStuff, path: String): String? {
    val typeDesc = when (val type = value["type"]) {
        is JSUndefined -> null
        is JSString -> type.str
        is JSObject -> {
            // If it is exactly "type", captured by a function
            if (
                type["type"].castOrNull<JSString>()?.str == "type" &&
                type["name"].castOrNull<JSString>()?.str == "type"
            ) null
            else failWith(
                "Type descriptor should either be missing or be a string, not ${type.javaClass.simpleName}",
                path
            )
        }

        else -> failWith(
            "Type descriptor should either be missing or be a string, not ${type.javaClass.simpleName}",
            path
        )
    }
    val funcDesc = when (typeDesc) {
        "context", "style", "layout", "locate" -> null
        else -> when (val func = value["func"]) {
            is JSUndefined -> null
            is JSString -> func.str
            else -> failWith(
                "Subtype descriptor should either be missing or be a string, not ${func.javaClass.simpleName}",
                path
            )
        }
    }

    val desc = when ((typeDesc != null) to (funcDesc != null)) {
        true to true -> {
            when (typeDesc) {
                "func" -> throw SerializationException("Dynamic functions aren't supported")
                "gradient" -> "gradient." + funcDesc!!
                "content" -> funcDesc
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

        true to false -> when (typeDesc) {
            "counter-update" -> "counter.update"
            "state-update" -> "state.update"
            "set-rule" -> "set-" + value["elem"].cast<JSString>().str
            "style" -> "style-deprecated"
            "layout" -> "layout"
            "relative" -> "relative-impl"
            "alignment" -> "alignment-impl"
            "func" -> "function"
            else -> typeDesc!!
        }

        false to true -> {
            funcDesc!!
        }

        else -> null
    }
    return desc
}

//inline fun <reified T : TValue> deserialize(value: JSStuff): TValue = deserialize(value)!!

/*
@file:OptIn(ExperimentalContracts::class)
@file:JvmName("ManualDeserialization")

package org.ldemetrios.tyko.model

import kotlinx.serialization.SerializationException
import org.ldemetrios.js.*
import org.ldemetrios.js.get
import org.ldemetrios.parsers.fail
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castOrNull
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.primaryConstructor

internal val labelType = ConcreteType("label")

class TypstSerialException : SerializationException {
    public constructor()
    public constructor(message: String?) : super(message)
    public constructor(message: String?, cause: Throwable?) : super(message, cause)
    public constructor(cause: Throwable?) : super(cause)
}

@OptIn(ExperimentalContracts::class)
inline fun check(value: Boolean, path: String, message: () -> String) {
    contract {
        returns() implies value
    }
    if (!value) failWith(message(), path)
}

fun failWith(message: String, path: String): Nothing = throw TypstSerialException("$message (at path `$path`)")

fun KType.inferClassifier(path: String) = when (val classifier = this.classifier) {
    is KClass<*> -> classifier
    null -> failWith("Type `$this` has null classifier", path)
    is KTypeParameter -> failWith(
        "TypeParameters are not supported as types for deserialization yet. Please specify concrete type.",
        path
    )
    else -> failWith(
        "Unknown classifier kind `$classifier` (instance of `${classifier.javaClass}`) for type `$this`",
        path
    )
}

@PublishedApi
internal fun deserialize(value: JSStuff, type: KType, path: String = ""): TValue? = when (value) {
    is JSUndefined -> if (type.isMarkedNullable) null else failWith("Missing value", path)
    is JSNull -> {
        check(
            type.containsUnparameterized("none", path),
            path
        ) { "None of `$type` can be represented with js null, only `none` can" }
        TNone
    }

    is JSBoolean -> {
        check(
            type.containsUnparameterized("bool", path),
            path
        ) { "None of `$type` can be represented with js boolean, only `bool` can" }
        TBool(value.toBoolean())
    }

    is JSNumber -> {
        val int = type.containsUnparameterized("int", path)
        val float = type.containsUnparameterized("float", path)
        when {
            int && float -> value.toString().toLongOrNull()?.let(::TInt) ?: TFloat(value.toDouble())
            int -> TInt(value.toString().toLongOrNull() ?: failWith("$value can't express `int`, only `float`", path))
            float -> TFloat(value.toDouble())
            else -> failWith("None of `$type` can be represented with js number, only `int` or `float` can", path)
        }
    }

    is JSArray -> {
        val elementType: KType =
            inferArrayElementType(type, path) ?: failWith("Can't infer array element type from type `$type`", path)
        TArray(
            value.mapIndexed { ind, it ->
                deserialize(it, elementType, "$path/$ind") ?: failWith("Missing element $ind", path)
            }
        )
    }

    is JSString -> {
        val classifier = type.inferClassifier(path)
        val str = TStr(value.str)
        if (classifier.isInstance(str)) {
            str
        } else {
            failWith("String `${value.str}` is not an instance of $type", path)
        }
    }

    is JSObject -> {
        val desc = inferDescriptor(value, path)
        if (desc != null) {
            concreteType(type, desc, value, path)
        } else {
            dictionaryType(type, value, path)
        }
    }

    else -> throw AssertionError()
}

private fun dictionaryType(type: KType, value: JSObject, path: String): TValue? {
    return when (val classifier = type.inferClassifier(path)) {
        TLocation::class -> location(value, path) as TValue?
        TPoint::class -> point(value, path) as TValue?
        TSides::class -> sides(value, type.arguments[0].toType(), path) as TValue?
        TMargin::class -> margin(value, type.arguments[0].toType(), path) as TValue?
        TDictionary::class -> {
            try {
                if (type.isSupertypeOf(typeOf<TLocation>())) return location(value, path) as TValue?
            } catch (_: TypstSerialException) {
            }
            try {
                if (type.isSupertypeOf(typeOf<TPoint>())) return point(value, path) as TValue?
            } catch (_: TypstSerialException) {
            }
            TDictionary<TValue>(
                value.mapValues { (k, v) ->
                    deserialize(v, type.arguments[0].toType(), "$path/$k") ?: failWith("Missing element `$k`", path)
                }
            ) as TValue?
        }
        TValue::class -> dictionaryType(typeOf<TDictionary<TValue>>(), value, path)
        else -> {
            val union = classifier.annotations.filterIsInstance<TUnionType>().singleOrNull()
            check(
                union != null,
                path
            ) { "`$type` can't be represented by js object, only synthetic type, TDictionary, union which contains said types as an option, or TValue" }
            val locOption = union.optionOf("location", type, path)
            val pointOption = union.optionOf("point", type, path)
            val sidesOption = union.optionOf("sides", type, path)
            val marginOption = union.optionOf("margin", type, path)
            val dictOption = union.optionOf("dictionary", type, path)
            when {
                locOption != null -> location(value, path)
                pointOption != null -> point(value, path)
                sidesOption != null -> sides(value, sidesOption[0].toType(), path)
                marginOption != null -> margin(value, marginOption[0].toType(), path)
                dictOption != null -> dictionaryType(TDictionary::class.createType(dictOption), value, path)
                else -> failWith(
                    "Nothing is the union [${union.options.joinToString(", ")}] can be represented by js object",
                    path
                )
            } as TValue?
        }
    }
}

private fun JSObject.getNullable(key: String) = this[key].let { if (it is JSUndefined) null else it }

private fun location(value: JSObject, path: String): TValue {
    check(value.size == 3, path) { "`location` should be represented by js object with exactly three entries" }
    return TLocation(
        deserialize(
            value.getNullable("page") ?: failWith("Missing `page` key", path),
            typeOf<TInt>(),
            "$path/page"
        ) as TInt,
        deserialize(
            value.getNullable("x") ?: failWith("Missing `x` key", path),
            typeOf<TLength>(),
            "$path/x"
        ) as TLength,
        deserialize(
            value.getNullable("y") ?: failWith("Missing `y` key", path),
            typeOf<TLength>(),
            "$path/y"
        ) as TLength,
    )
}


private fun point(value: JSObject, path: String): TValue {
    check(value.size == 2, path) { "`point` should be represented by js object with exactly three entries" }
    return TPoint(
        deserialize(
            value.getNullable("x") ?: failWith("Missing `x` key", path),
            typeOf<TLength>(),
            "$path/x"
        ) as TLength,
        deserialize(
            value.getNullable("y") ?: failWith("Missing `y` key", path),
            typeOf<TLength>(),
            "$path/y"
        ) as TLength,
    )
}

private fun sides(value: JSObject, type: KType, path: String): TValue {
    check(value.size <= 4, path) { "`sides` should be represented by js object with <= 4 entries" }
    check(value.keys.isSubsetOf(setOf("top", "right", "bottom", "left")), path) {
        "`sides` should be represented by js object with keys in [top, right, bottom, left]"
    }
    return TSides(
        deserialize(value.getNullable("top") ?: failWith("Missing `top` key", path), type, "$path/top") as TLength,
        deserialize(
            value.getNullable("right") ?: failWith("Missing `right` key", path),
            type,
            "$path/right"
        ) as TLength,
        deserialize(
            value.getNullable("bottom") ?: failWith("Missing `bottom` key", path),
            type,
            "$path/bottom"
        ) as TLength,
        deserialize(value.getNullable("left") ?: failWith("Missing `left` key", path), type, "$path/left") as TLength,
    )
}

private fun margin(value: JSObject, type: KType, path: String): TValue {
    check(value.size <= 6, path) { "`margin` should be represented by js object with <= 6 entries" }
    check(value.keys.isSubsetOf(setOf("top", "right", "bottom", "left", "inside", "outside")), path) {
        "`margin` should be represented by js object with keys in [top, right, bottom, left, inside, outside]"
    }
    return TMargin(
        deserialize(value.getNullable("top") ?: failWith("Missing `top` key", path), type, "$path/top") as TLength,
        deserialize(
            value.getNullable("right") ?: failWith("Missing `right` key", path),
            type,
            "$path/right"
        ) as TLength,
        deserialize(
            value.getNullable("bottom") ?: failWith("Missing `bottom` key", path),
            type,
            "$path/bottom"
        ) as TLength,
        deserialize(
            value.getNullable("inside") ?: failWith("Missing `inside` key", path),
            type,
            "$path/inside"
        ) as TLength,
        deserialize(value.getNullable("left") ?: failWith("Missing `left` key", path), type, "$path/left") as TLength,
        deserialize(
            value.getNullable("outside") ?: failWith("Missing `outside` key", path),
            type,
            "$path/outside"
        ) as TLength,
    )
}

private fun <T> Set<T>.isSubsetOf(other: Set<T>) = this.all { it in other }

private fun concreteType(type: KType, desc: String, value: JSObject, path: String): TValue {
    val classifier = type.inferClassifier(path)

    val actualClassifier = interfaceTypes[desc] ?: setInterfaceTypes[desc] ?: when (desc) {
        "element" -> TElement::class
        "type" -> TType::class
        else -> failWith(
            "Can't find interface type for `$desc` descriptor", path
        )
    }

    check(classifier.isSuperclassOf(actualClassifier), path) { "`$desc` is not subclass of `$type`" }

    val annotation = classifier.annotations
    val unionAnn = annotation.filterIsInstance<TUnionType>().singleOrNull()
    val interfaceAnn = annotation.filterIsInstance<TInterfaceType>().singleOrNull()

    val params = when {
        classifier.typeParameters.isEmpty() -> listOf()
        unionAnn != null -> {
            val option = unionAnn.optionOf(desc, type, path)
            check(option != null, path) { "$desc is not in the union [${unionAnn.options.joinToString(", ")}]" }
            option.map { it.toType() }
        }
        interfaceAnn != null -> {
            classifier.typeParameters.map { it.toType() }
        }
//        classifier == TValue::class -> {
//            val impl = interfaceTypes[desc] ?: setInterfaceTypes[desc] ?: failWith(
//                "Can't find interface type for `$desc` descriptor", path
//            )
//            impl to impl.typeParameters.map { it.toType() }
//        }
//        classifier == TSetRule -> {
//            if (classifier )
//            check(desc.startsWith("set-"), path) { "" }
//            val impl = setInterfaceTypes[desc] ?: failWith("Can't find set rule `$desc` impl", path)
//            check(classifier.isSuperclassOf(impl), path) { "$type is not supertype of $impl" }
//            impl to listOf()
//        }
//        union != null -> {
//            val parameters = union.optionOf(desc, type, path)?.map { it.toType() } ?: failWith(
//                "Descriptor `$desc` is not in the union [${union.options.joinToString(", ")}] represented by type `$type`",
//                path
//            )
//            val clazz = interfaceTypes[desc] ?: setInterfaceTypes[desc] ?: failWith(
//                "Can't find interface type for `$desc` descriptor", path
//            )
//            clazz to parameters
//        }
//        synthetic != null || enum != null -> failWith(
//            "`$type` can't be represented with js object, as it is synthetic or enum",
//            path
//        )
//        interf != null -> {
//            if (desc.startsWith("set-")) {
//                val impl = setInterfaceTypes[desc] ?: failWith("Can't find set rule `$desc` impl", path)
//                check(classifier.isSuperclassOf(impl), path) { "$type is not supertype of $impl" }
//                impl to listOf()
//            } else {
//                val clazz = interfaceTypes[desc] ?: failWith("Can't find interface type for `$desc` descriptor", path)
//                val chain = clazz.annotations.filterIsInstance<TInterfaceType>().single().chain
//                check(chain.contains(interf.name), path) { "${interf.name} can't represent with `$desc`" }
//                clazz to classifier.typeParameters.map { it.toType() }
//            }
//        }
//        set != null -> {
//            check(desc == "set-" + set.owner, path) {
//                "`set-${set.owner}` (represented with `$type`) expected, `$desc` found"
//            }
//            setInterfaceTypes[desc]!! to type.arguments.map { it.toType() }
//        }
        else -> throw AssertionError("$desc, $type")
    }

    val impl =
        actualClassifier.annotations.filterIsInstance<TInterfaceType>().singleOrNull()?.impl
            ?: actualClassifier.annotations.filterIsInstance<TSetRuleType>().singleOrNull()?.impl
            ?: failWith("No TInterfaceType annotation for $actualClassifier", path)

    val fields = impl.primaryConstructor!!.parameters.associateWith {
        val itsName = it.annotations.filterIsInstance<SerialName>().singleOrNull()?.name ?: failWith("WTF", path)
        val itsType = it.type.substitute(impl.typeParameters.zip(params).toMap(), "$path/$itsName")
        deserialize(value[itsName], itsType, "$path/$itsName")
    }
    return impl.primaryConstructor!!.callBy(fields) as TValue
}

private fun KType.substitute(map: Map<KTypeParameter, KType>, path: String): KType {
    val classifier = this.classifier ?: failWith("Null classifier of `$this`", path)
    val nullable = this.isMarkedNullable
    if (classifier is KTypeParameter && classifier in map) {
        val substituted = map[classifier]!!
        return substituted.classifier!!.createType(substituted.arguments, nullable || substituted.isMarkedNullable)
    }
    val substitutedArguments = this.arguments.mapIndexed { index, it ->
        when (it.variance) {
            null -> typeOf<TValue>()
            KVariance.INVARIANT -> it.type ?: typeOf<TValue>()
            KVariance.IN -> typeOf<TValue>()
            KVariance.OUT -> it.type ?: typeOf<TValue>()
        }.substitute(map, "$path::substituting/$index")
    }
    return classifier.createType(substitutedArguments.map { KTypeProjection.invariant(it) }, nullable)
}

private fun TUnionType.optionOf(desc: String, type: KType, path: String): List<KTypeProjection>? {
    val supertypes = interfaceTypes[desc]?.let {
        it.annotations.filterIsInstance<TInterfaceType>().singleOrNull()?.chain?.toList()
    } ?: setInterfaceTypes[desc]?.let {
        listOf("set-" + it.annotations.filterIsInstance<TSetRuleType>().single().owner, "style", "content")
    } ?: when (desc) {
        "sides" -> listOf("sides", "margin")
        // smth more?
        else -> listOf(desc)
    }// failWith("Can't find interface type for `$desc` descriptor", path)
    val descriptor = options.find { it in supertypes } ?: return null
    val before = options.takeWhile { it != descriptor }.sumOf { genericTypes[it]!! }
    return type.arguments.subList(before, before + genericTypes[descriptor]!!)
}

fun inferArrayElementType(type: KType, path: String): KType? {
    val classifier = type.inferClassifier(path)
    val annotation = classifier.annotations
    val union = annotation.filterIsInstance<TUnionType>().singleOrNull()
    return when {
        union != null -> {
            union.optionOf("array", type, path)?.single()?.toType() ?: failWith(
                "Can't find array element type in union `$type`", path
            )
        }
        classifier == TArray::class -> type.arguments[0].toType()
        classifier == TValue::class -> typeOf<TValue>()
        else -> null
    }
}

internal fun KTypeProjection.toType() = when (variance) {
    null -> typeOf<TValue>()
    KVariance.INVARIANT -> type ?: typeOf<TValue>()
    KVariance.IN -> typeOf<TValue>()
    KVariance.OUT -> type ?: typeOf<TValue>()
}

internal fun KTypeParameter.toType() = when (variance) {
    KVariance.INVARIANT -> upperBounds.singleOrNull() ?: typeOf<TValue>()
    KVariance.IN -> typeOf<TValue>()
    KVariance.OUT -> upperBounds.singleOrNull() ?: typeOf<TValue>()
}

internal fun KType.containsUnparameterized(opt: String, path: String): Boolean {
    val classifier = this.inferClassifier(path)
    val annotation = classifier.annotations
    val union = annotation.filterIsInstance<TUnionType>().singleOrNull()
    val synthetic = annotation.filterIsInstance<TSyntheticType>().singleOrNull()
    val enum = annotation.filterIsInstance<TEnumType>().singleOrNull()
    val set = annotation.filterIsInstance<TSetRuleType>().singleOrNull()
    val interf = annotation.filterIsInstance<TInterfaceType>().singleOrNull()

    return when {
        union != null -> union.options.contains(opt)
        synthetic != null -> synthetic.options.contains(opt) || opt == "dictionary"
        enum != null -> enum.options.contains(opt)
        set != null -> opt.startsWith("set-") && set.owner == opt.drop(4)
        interf != null -> interf.chain.contains(opt)
        else -> when (classifier) {
            TValue::class -> true
            TSetRule::class -> opt.startsWith("set-")
            else -> failWith("Invalid classifier `$classifier` inside `KType.containsUnparameterized`", path)
        }
    }
}

private fun inferDescriptor(value: JSStuff, path: String): String? {
    val typeDesc = when (val type = value["type"]) {
        is JSUndefined -> null
        is JSString -> type.str
        is JSObject -> {
            // If it is exactly "type", captured by a function
            if (
                type["type"].castOrNull<JSString>()?.str == "type" &&
                type["name"].castOrNull<JSString>()?.str == "type"
            ) null
            else failWith(
                "Type descriptor should either be missing or be a string, not ${type.javaClass.simpleName}",
                path
            )
        }

        else -> failWith(
            "Type descriptor should either be missing or be a string, not ${type.javaClass.simpleName}",
            path
        )
    }
    val funcDesc = when (typeDesc) {
        "context", "style", "layout", "locate" -> null
        else -> when (val func = value["func"]) {
            is JSUndefined -> null
            is JSString -> func.str
            else -> failWith(
                "Subtype descriptor should either be missing or be a string, not ${func.javaClass.simpleName}",
                path
            )
        }
    }

    val desc = when ((typeDesc != null) to (funcDesc != null)) {
        true to true -> {
            when (typeDesc) {
                "func" -> throw SerializationException("Dynamic functions aren't supported")
                "gradient" -> "gradient." + funcDesc!!
                "content" -> funcDesc
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

        true to false -> when (typeDesc) {
            "counter-update" -> "counter.update"
            "state-update" -> "state.update"
            "set-rule" -> "set-" + value["elem"].cast<JSString>().str
            "style" -> "style-deprecated"
            "layout" -> "layout"
            "relative" -> "relative-impl"
            "alignment" -> "alignment-impl"
            "func" -> "function"
            else -> typeDesc!!
        }

        false to true -> {
            funcDesc!!
        }

        else -> null
    }
    return desc
}

inline fun <reified T : TValue> deserialize(value: JSStuff): TValue = deserialize(value, typeOf<T>())!!


 */


fun deserialize(input: String): TValue? {
    if (input.isBlank()) return null
    val raw = JSParser.parseValue(input)
//    println(raw.toString(4))
    return deserialize(raw)
}