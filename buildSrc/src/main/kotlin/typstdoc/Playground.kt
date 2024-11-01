package typstdoc

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.ldemetrios.js.*
import org.ldemetrios.parsers.format
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.exec
import org.ldemetrios.utilities.invoke
import java.io.File

fun main() {
    val entries = File("docs.json")
        .readText()
        .let { JSParser.parseValue(it) }
        .cast<JSArray>()[0]
        .cast<JSObject>()["children"]
        .cast<JSArray>()
        .flatMap {
            it.cast<JSObject>()["children"]
                .cast<JSArray>()
        }
        .map {
            it.cast<JSObject>()["body"]
                .cast<JSObject>()
        }

    fun List<JSObject>.ofType(str: String) =
        filter { it["kind"].cast<JSString>().str == str }.map { it["content"].cast<JSObject>() }

    val groups = entries.ofType("group")

    val types = entries.ofType("type")
    val funcs = (
            entries.ofType("func") +
                    groups.flatMap { it["functions"].cast<JSArray>() }
            ).map { it.cast<JSObject>() }
    val symbols = entries.ofType("symbol")

    val x = funcs.flatMap {
        val lst = mutableListOf<JSObject>()
        var step = listOf(it)
        while (step.isNotEmpty()) {
            lst.addAll(step)
            step = step.flatMap { it["scope"].cast<JSArray>() }.map { it.cast<JSObject>() }
        }
        lst
    }
        .filter {
            it["element"].cast<JSBoolean>().toBoolean()
        }
        .map {
            json.decodeFromString<FunctionData>(it.toString())
        }


    File("buildSrc/src/main/resources/types/__automated").run {
        parentFile.mkdirs()
        if (exists()) delete()
        createNewFile()

        writeText(x.joinToString("\n"))
    }

    File("buildSrc/src/main/resources/types/__automated_enums").run {
        parentFile.mkdirs()
        if (exists()) delete()
        createNewFile()

        writeText(x.map { it.enums() }.filter { it.isNotBlank() }.joinToString("\n"))
    }

    File("buildSrc/src/main/resources/types/__scoped-funcs").run {
        parentFile.mkdirs()
        if (exists()) delete()
        createNewFile()

        writeText(
            (types.flatMap { it["scope"].cast<JSArray>() } + funcs)
                .filterNot { it["element"].toBoolean() }
                .map {
                    (it["path"].cast<JSArray>().map { it.cast<JSString>().str } + it["name"].cast<JSString>().str)
                        .joinToString(".")
                }.joinToString("\n")
        )
    }
}

val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
}


@Serializable
data class FunctionData(
    val path: List<String>,
    val name: String,
//    val title: String,
//    val keywords: List<String>,
//    val element: Boolean,
    val contextual: Boolean,
//    val details: String,
//    val example: String?,
//    val self: Boolean, // Always false
    val params: List<ParamData>,
//    val returns: List<String>, // Always [ "content" ]
) {
    override fun toString(): String = StringBuilder().apply {
        val fullName = path + name
        if (contextual) append("contextual ")
        append("element ")
        append(fullName.joinToString("."))
        append("(\n")
        for (param in params) {
            append("\t")
            append(param.toString(fullName))
            append(",\n")
        }
        append(") -> content\n")
    }.toString()

    fun enums(): String = StringBuilder().apply {
        val fullName = path + name
        for (param in params) if (param.strings.isNotEmpty()) {
            append("enum ")
            append(fullName.joinToString("."))
            append("-")
            append(param.name)
            append("(\n")
            for (variant in param.strings) {
                append("\t")
                append(variant.string)
                append(",\n")
            }
            append(")\n\n")
        }
    }.toString()
}

@Serializable
data class ParamData(
    val name: String,
//    val details: String,
//    val example: String?,
    val types: List<String>,
    val strings: List<StringDetails>,
//    val default: String?,
    val positional: Boolean,
    val named: Boolean,
    val required: Boolean,
    val variadic: Boolean,
    val settable: Boolean,
) {
    init {
        require(positional xor named) { "$name $positional $named" }
    }

    fun toString(parent: List<String>): String = StringBuilder().apply {
        if (required) append("req ")
        if (positional) append("pos ")
        if (variadic) append("var ")
        if (settable) append("set ")
        append(name)
        append(": ")
        append(types.joinToString("|") {
            if (strings.isEmpty() || it != "str") {
                it
            } else {
                parent.joinToString(".") + "-" + name
            }
        })
    }.toString()
}

@Serializable
data class StringDetails(
    val string: String,
//    val details: String,
)

fun JSArray.examine() = forEach {
    println(it.javaClass)
}

fun JSObject.examine() = keys.forEach(::println)

fun JSStuff.examine() = println(this)
