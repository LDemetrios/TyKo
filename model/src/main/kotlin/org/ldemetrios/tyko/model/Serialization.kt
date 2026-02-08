package org.ldemetrios.tyko.model


import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import java.lang.AssertionError
import kotlin.io.encoding.Base64
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVariance
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.typeOf

@MustBeDocumented
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
public annotation class SerialName(val value: String)

private operator fun KType.get(x: Int) = this.arguments[x]
private operator fun KClassifier.invoke(vararg args: KType) =
    this.createType(args.map { KTypeProjection(KVariance.INVARIANT, it) })

private operator fun KClassifier.invoke(vararg args: KTypeProjection) = this.createType(args.asList())

fun subtypes(type: KType): List<KType> {
    when (type.classifier) {
        IntoValue::class, null -> return listOf(typeOf<TValue>())
        IntoArr::class -> return listOf(TArray::class(type[0]))
        IntoDict::class -> return listOf(TDict::class(type[0]))
        IntoCollection::class -> return listOf(TCollection::class(type[0]))
        IntoStr::class -> return listOf(typeOf<TStr>())
        Margin::class -> return listOf(type[0].type!!, MarginImpl::class(type[0]))
        MarginSplat::class -> return listOf(type[0].type!!)
        Corners::class -> return listOf(type[0].type!!, CornersImpl::class(type[0]))
        CornersSplat::class -> return listOf(type[0].type!!)
        Sides::class -> return listOf(type[0].type!!, SidesImpl::class(type[0]))
        SidesSplat::class -> return listOf(type[0].type!!)
        DataSourceOrPreset::class -> return listOf(type[0].type!!, typeOf<DataSource>())
        Smart::class -> return listOf(type[0].type!!, typeOf<TAuto>())
        ArrayOrSingle::class -> return listOf(type[0].type!!, TArray::class(type[0]))
        Progression::class -> return listOf(type[0].type!!, TArray::class(type[0]), typeOf<TFunc>())
        Option::class -> return listOf(type[0].type!!, typeOf<TNone>())
        Computable::class -> return listOf(type[0].type!!, typeOf<TFunc>())
        TRelativeBase::class -> return listOf(typeOf<TLength>(), typeOf<TRatio>())
        else -> return (type.classifier as KClass<*>).sealedSubclasses.map {
            it.starProjectedType
        }
    }
}

fun options(type: KType): List<KType> {
    val result = mutableSetOf<KType>()
    var step = setOf(type)
    while (step.isNotEmpty()) {
        result.addAll(step)
        step = step.flatMap { subtypes(it) }.toSet()
    }
    return result.toList()
}

fun <T> MutableSet<T>.prefer(what: T, over: T): MutableSet<T> {
    if (what in this) remove(over)
    return this
}

fun selectType(obj: JsonObject, typeDiscriminator: String, expectedType: KType, jsonPath: String): KType {
    val discriminatedTypes: MutableMap<String, List<KType>> = options(expectedType)
        .groupBy {
            (it.classifier!! as KClass<*>)
                .annotations
                .filterIsInstance<SerialName>()
                .singleOrNull()
                ?.value
        }
        .filterKeys { it != null }
        .toMutableMap() as MutableMap<String, List<KType>>
//        .mapValues { (k, v) ->
//            val v = v.toMutableSet()
//                .prefer(typeOf<TStr>(), typeOf<TPath>())
//                .prefer(typeOf<TElement>(), typeOf<TLocatableElement<*>>())
//            v.singleOrNull()
//                ?: v.singleOrNull { TValue::class.java.isAssignableFrom((it.classifier!! as KClass<*>).java) }
//                ?:
//        }

    val dict = ((discriminatedTypes["dict"] ?: listOf()) + (discriminatedTypes["stroke"] ?: listOf()))
    if (dict.isNotEmpty()) discriminatedTypes["dict"] = dict

    discriminatedTypes[typeDiscriminator]?.singleOrNull()?.let { return it }

    val options = discriminatedTypes[typeDiscriminator] ?: listOf()
    when (typeDiscriminator) {
        "element" -> when {
            typeOf<TElement>() in options -> return typeOf<TElement>()
            typeOf<TLocatableElement<*>>() in options -> return typeOf<TLocatableElement<*>>()
        }

        "dict" -> {
            options.find { it.classifier == TDict::class }?.let { return it }

            val value = obj["value"]!!.jsonObject

            if ("name" in value.keys) {
                options.find { it.classifier == TFontDescriptorImpl::class }?.let { return it }
                throw AssertionError("Can't choose dict with `name` out of $options (expected $expectedType type at $jsonPath)")
            }

            if ("encoding" in value.keys) {
                options.find { it.classifier == TImageFormatDict::class }?.let { return it }
                throw AssertionError("Can't choose dict with `encoding` out of $options (expected $expectedType type at $jsonPath)")
            }
            if ("amount" in value.keys) {
                options.find { it.classifier == FirstLineIndentImpl::class }?.let { return it }
                throw AssertionError("Can't choose dict with `amount` out of $options (expected $expectedType type at $jsonPath)")
            }

            if ("page" in value.keys) {
                options.find { it.classifier == LocationOnPage::class }?.let { return it }
                throw AssertionError("Can't choose dict with `page` out of $options (expected $expectedType type at $jsonPath)")
            }

            val restOptions = options.filter { it.classifier in POSSIBLY_EMPTY_DICT_CLASSES }
            restOptions.singleOrNull()?.let { return it }

            if (value.isEmpty()) throw AssertionError("Can't choose empty dict out of $options (expected $expectedType type at $jsonPath)")

            val maybeStroke = options.find { it.classifier == TStrokeImpl::class }
            if (maybeStroke != null && value.keys.all { it in STROKE_KEYS }) return maybeStroke

            val maybeSmartquote = options.find { it.classifier == TSmartquoteSymbols::class }
            if (maybeSmartquote != null && value.keys.all { it in SMARTQUOTE_KEYS }) return maybeSmartquote

            val maybeAugment = options.find { it.classifier == TMatAugmentDict::class }
            if (maybeAugment != null && value.keys.all { it in MAT_AUGMENT_KEYS }) return maybeAugment

            val maybeSides = options.find { it.classifier == SidesImpl::class }
            val maybeMargin = options.find { it.classifier == MarginImpl::class }
            val maybeCorners = options.find { it.classifier == Corners::class }

            when (Triple(
                maybeSides != null && value.keys.all { it in SIDES_KEYS },
                maybeMargin != null && value.keys.all { it in MARGIN_KEYS },
                maybeCorners != null && value.keys.all { it in CORNERS_KEYS }
            )) {
                Triple(true, false, false) -> return maybeSides!!
                Triple(false, true, false) -> return maybeMargin!!
                Triple(false, false, true) -> return maybeCorners!!

                Triple(true, true, false) -> return maybeSides!! // Prefer Sides to Margin
            }
        }
    }
    throw AssertionError("Can't choose $typeDiscriminator out of $options (expected $expectedType type at $jsonPath)")
}

private fun <T> Iterable<T>.filterIfAny(predicate: (T) -> Boolean) = filter(predicate).ifEmpty { this }
val STROKE_KEYS = setOf("paint", "thickness", "cap", "join", "dash", "miter-limit")
val SIDES_KEYS = setOf("top", "right", "bottom", "left", "x", "y", "rest")
val MARGIN_KEYS = setOf("top", "right", "bottom", "left", "inside", "outside", "x", "y", "rest")
val SMARTQUOTE_KEYS = setOf("single", "double")
val MAT_AUGMENT_KEYS = setOf("hline", "vline", "stroke")
val CORNERS_KEYS = setOf("top-left", "top-right", "bottom-left", "bottom-right", "left", "top", "right", "bottom")
val POSSIBLY_EMPTY_DICT_CLASSES = setOf(
    TStrokeImpl::class, SidesImpl::class, MarginImpl::class, TSmartquoteSymbols::class,
    TMatAugmentDict::class, Corners::class
)

fun deserializeAs(json: JsonElement, expectedType: KType, jsonPath: String): Any? {
    if (json is JsonNull) {
        if (expectedType.isMarkedNullable) return null as Any?
        throw AssertionError("Can't deserialize $json as $expectedType at $jsonPath")
    }
    when (json) {
        is JsonPrimitive -> return when (expectedType.classifier) {
            Int::class -> json.jsonPrimitive.int
            Long::class -> json.jsonPrimitive.long
            Double::class -> {
                val prim = json.jsonPrimitive
                if (prim.isString) {
                    when (prim.content) {
                        "inf" -> Double.POSITIVE_INFINITY
                        "-inf" -> Double.NEGATIVE_INFINITY
                        "nan" -> Double.NaN
                        else -> prim.double
                    }
                } else {
                    prim.double
                }
            }

            Boolean::class -> json.jsonPrimitive.boolean
            String::class -> json.jsonPrimitive.content
            ByteArray::class -> Base64.decode(json.jsonPrimitive.content)
            else -> throw AssertionError("Can't deserialize $json as $expectedType at $jsonPath")
        }

        is JsonArray -> return when (expectedType.classifier) {
            List::class -> json.jsonArray.mapIndexed { idx, it ->
                deserializeAs(
                    it,
                    expectedType.arguments[0].type!!,
                    "$jsonPath/$idx"
                )
            }

            else -> throw AssertionError("Can't deserialize $json as $expectedType at $jsonPath")
        }

        else -> {
            var obj = json.jsonObject
            var typeDiscriminator = obj["type"]?.jsonPrimitive?.content
                ?: throw AssertionError("Missing type discriminator at $json at $jsonPath")

            if (typeDiscriminator == "set-rule") {
                val origin = obj["original"]
                if (origin == JsonNull || origin == null) {
                    val (newObj, parsed) = preprocessSetRule(obj)
                    if (parsed != null) return parsed
                    obj = newObj!!
                } else {
                    obj = JsonObject(
                        mapOf(
                            "type" to JsonPrimitive("set-" + obj["elem"]!!.jsonPrimitive.content),
                            obj["id"]!!.jsonPrimitive.content to origin,
                            "internals" to obj["internals"]!!,
                        )
                    )
                }
            }

            typeDiscriminator = obj["type"]!!.jsonPrimitive.content

            val type = selectType(obj, typeDiscriminator, expectedType, jsonPath)
            val klass = type.classifier!! as KClass<*>
            when (klass) {
                TNone::class -> return TNone
                TAuto::class -> return TAuto
                LineWidth::class -> return LineWidth
                TDict::class -> return TDict(obj["value"]!!.jsonObject.mapValues {
                    deserializeAs(
                        it.value,
                        type[0].type ?: typeOf<TValue>(),
                        "$jsonPath/${it.key}"
                    ) as IntoValue
                })

                SidesImpl::class -> return Sides.fromValue(
                    deserializeAs(json, TDict::class(expectedType[0]), jsonPath) as TValue,
                    Sides::class(expectedType[0].type ?: typeOf<TValue>()),
                )

                MarginImpl::class -> return Margin.fromValue(
                    deserializeAs(json, TDict::class(expectedType[0]), jsonPath) as TValue,
                    Margin::class(expectedType[0].type ?: typeOf<TValue>()),
                )

                CornersImpl::class -> return Corners.fromValue(
                    deserializeAs(json, TDict::class(expectedType[0]), jsonPath) as TValue,
                    Corners::class(expectedType[0].type ?: typeOf<TValue>()),
                )

                else -> if (klass.java.isEnum) {
                    val name = obj["value"]!!.jsonPrimitive.content
                    return klass.java.enumConstants.find {
                        val repr = (it as? IntoStr)?.intoValue()?.value
                            ?: (it as Enum<*>).name.lowercase()
                        repr == name
                    }!!
                }
            }
            val constructor = klass.primaryConstructor!!
            val parameters = constructor.parameters
                .map { it.type.resolveParameter(type) }

            if (typeDiscriminator == "array" && (type.classifier == TArray::class || type.classifier == DashArray::class)) {
                if (type.classifier == TArray::class) {
                    return TArray(
                        obj["value"]!!.jsonArray.mapIndexed { idx, it ->
                            deserializeAs(
                                it,
                                type[0].type ?: typeOf<TValue>(),
                                "$jsonPath/$idx"
                            ) as IntoValue
                        }
                    )
                } else {
                    return DashArray(
                        obj["value"]!!.jsonArray.mapIndexed { idx, it ->
                            deserializeAs(
                                it,
                                typeOf<DashLength>(),
                                "$jsonPath/$idx"
                            ) as DashLength
                        }
                    )
                }
            }
            val arguments = if (typeDiscriminator == "array") {
                val arr = obj["value"]!!.jsonArray
                parameters.mapIndexed { idx, it ->
                    deserializeAs(arr[idx], it, "$jsonPath/${constructor.parameters[idx].name}")
                }
            } else {
                val source = if (typeDiscriminator == "dict") obj["value"]!!.jsonObject else obj
                parameters.mapIndexed { idx, it ->
                    val name = constructor.parameters[idx].findAnnotations<SerialName>().singleOrNull()?.value
                        ?: camelToKebab(constructor.parameters[idx].name!!)
                    val value = source[name]
                    value?.let { json ->
                        deserializeAs(json, it, "$jsonPath/${constructor.parameters[idx].name}")
                    }
                }
            }

            try {
                return constructor.call(*arguments.toTypedArray())
            } catch (e: Exception) {
                throw RuntimeException(
                    "Failed to deserialize $obj as $expectedType (selected $klass), arguments inferred: $arguments, names ${
                        constructor.parameters.map {
                            it.findAnnotations<SerialName>().singleOrNull()?.value
                                ?: camelToKebab(it.name!!)
                        }
                    } at $jsonPath", e)
            }
        }
    }
}

private fun KType.resolveParameter(constructorOwner: KType): KType = when (val klass = this.classifier) {
    is KClass<*> -> klass.createType(
        arguments = this.arguments.map { it.copy(type = it.type?.resolveParameter(constructorOwner)) }
    )

    is KTypeParameter -> {
        val idx = (constructorOwner.classifier as KClass<*>).typeParameters.indexOf(klass)
        constructorOwner[idx].type ?: typeOf<IntoValue>()
    }

    else -> throw AssertionError("Can't resolve $this as parameter of $constructorOwner")
}

fun deserialize(json: JsonElement): TValue = deserializeAs(json, typeOf<TValue>(), "") as TValue

@OptIn(ExperimentalSerializationApi::class)
val prettyPrint = Json {
    encodeDefaults = true
    prettyPrint = true
    prettyPrintIndent = "    "
}

fun deserialize(str: String): TValue {
    val x = Json.decodeFromString<JsonElement>(str)
//    println(prettyPrint.encodeToString(x))
    return deserialize(x)
}

fun preprocessSetRule(json: JsonObject): Pair<JsonObject?, TValue?> {
    val elem = json["elem"]!!.jsonPrimitive.takeIf { it.isString }!!.content
    val id = json["id"]?.jsonPrimitive?.takeIf { it.isString }?.content
    val internalsJson = json["internals"]!!
    val internals = deserializeAs(internalsJson, typeOf<SetRuleInternals>(), "") as SetRuleInternals
    val value = json["value"]!!.jsonPrimitive.takeIf { it.isString }!!.content
    val reconstructed = json["reconstructed"]!!
    fun set(field: String, v: JsonElement) = JsonObject(
        mapOf(
            "type" to JsonPrimitive("set-$elem"),
            field to v,
            "internals" to internalsJson,
        )
    ) to null

    fun set(v: JsonElement) = set(id!!, v)

    fun show(func: String) = null to TShowRule(null, TNativeFunc(func))

    when (elem) {
        "page" -> when (id) {
            "width", "height" -> return set(reconstructed)
        }

        "text" -> when (id) {
            null -> when (internals.id) {
                38 -> when (val func = reconstructed.castOrNull<JsonObject>()?.get("value")?.maybeString) {
                    "lower", "upper" -> return show(func)
                }
            }
        }

        "math.equation" -> when (id) {
            null -> when (internals.id) {
                7 -> when (val func = reconstructed.castOrNull<JsonObject>()?.get("value")?.maybeString) {
                    "chancery" -> return show("math.cal")
                    "roundhand" -> return show("math.scr")
                    "monospace" -> return show("math.mono")
                    "fraktur" -> return show("math.frak")
                    "doublestruck" -> return show("math.bb")
                    "sansserif" -> return show("math.sans")
                    "plain" -> return show("math.serif")
                }
                8 -> TODO ("cramped")
                9 -> return show("math.bold")
                10 -> when (reconstructed.castOrNull<JsonObject>()?.get("value")?.maybeBool) {
                    true -> return show("math.italic")
                    false -> return show("math.upright")
                    null -> Unit
                }
            }
        }

        "table.cell" -> when (id) {
            null -> when (internals.id) {
                10 -> TODO("kind { Header(NonZeroU32, TableHeaderScope), Footer, #[default] Data }")
            }
        }
        "grid.cell" -> when (id) {
            null -> when (internals.id) {
                10 -> TODO("is_repeated")
            }
        }
    }
    throw UnrecognizedSetRule(elem, id, internals.id, json)
}

class UnrecognizedSetRule(val elem: String, val id: String?, val internalsId: Int, val json: JsonObject) : Exception(
    "Unrecognized set $elem(${id ?: internalsId}), initial json: $json"
)

val SOURCE = Regex(
    listOf(
        "(?<=[a-z])(?=[A-Z0-9])", // lowercase followed by uppercase or digit
        "(?<=[A-Z])(?=[A-Z][a-z])",  // uppercase followed by uppercase then lowercase
        "(?<=[0-9])(?=[A-Za-z])",    // digit followed by letter
    ).joinToString("|")
)

fun camelToKebab(str: String): String = str.replace(SOURCE, "-").lowercase()

val JsonElement.maybeString get() = castOrNull<JsonPrimitive>()?.takeIf { it.isString }?.content
val JsonElement.maybeBool get() = castOrNull<JsonPrimitive>()?.booleanOrNull
