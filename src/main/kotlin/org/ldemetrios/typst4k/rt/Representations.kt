package org.ldemetrios.typst4k.rt

import org.ldemetrios.typst4k.model.*
import org.ldemetrios.utilities.castOrNull
import java.lang.StringBuilder

typealias CommonInterfaceName = TValue

fun TValue.repr() = format()

data class ArgumentEntry(var variadic: Boolean, var name: String?, var value: TValue?) {
    fun repr(): String? = when {
        value == null -> null
        variadic -> (".." + value!!.repr())
        name != null -> (name + ": " + value!!.repr())
        else -> (value!!.repr())
    }

    val first get() = variadic
    val second get() = name
    val third get() = value
}

object Representations {
    fun <E : CommonInterfaceName> reprOf(value: List<E>): String = when (value.size) {
        0 -> "()"
        1 -> "(" + value[0].repr() + ",)"
        else -> "( " + value.joinToString(", ") { it.repr() } + ")"
    }

    fun reprOf(value: TStr): String = reprOf(value.value)


    fun <V : CommonInterfaceName> reprOf(value: Map<String, V>): String = when (value.size) {
        0 -> "(:)"
        else -> "( " + value.entries.joinToString(", ") { reprOf(it.key) + " : " + it.value.repr() } + ")"
    }

    fun reprOf(value: String): String = "\"" +
            value.replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\"", "\\\"") + "\""

    fun reprOf(value: Boolean): String = value.toString()
    fun reprOf(value: Long): String = value.toString()
    fun reprOf(value: Double): String = value.toString()


    fun <A : CommonInterfaceName> reprOf(value: TArguments<A>): String = "((..args) => args)(" +
            (value.named.value.map { it.key + " : " + it.value.repr() } +
                    value.positional.value.map { it.repr() }).joinToString(", ") + ")"

    fun reprOf(value: TSpace): String = "[ ]"
    fun reprOf(value: TElement): String = value.name.value
    fun reprOf(value: TSequence): String {
        if (value.children.value.isEmpty()) return "[]"
        return "{ " + value.children.value.joinToString("; ") { it.repr() } + "; }"
    }

    fun structRepr(
        name: String,
        vararg elements: ArgumentEntry, // vararg, name (null if positional), value
    ): String {
        when (name) {
            "none", "auto" -> return name
            "text" -> {
                val present = elements.filter { it.third != null }
                if (present.size == 1 && !present[0].first && present[0].second == null && present[0].third is TStr) {
                    return present[0].third!!.repr()
                }
            }

            "math.mat" -> {
                elements.find { it.name == "delim" }?.let { delim ->
                    delim.value = delim.value.castOrNull<TArray<*>>()?.value?.get(0) ?: delim.value
                }
                elements.find { it.name == "augment" }?.let { augment ->
                    val asDict = augment.value as? TDictionary<*> ?: return@let
                    val map = asDict.value.toMutableMap()
                    if (map["stroke"] is TAuto) map.remove("stroke")
                    augment.value = TDictionaryImpl(map)
                }
            }
        }

        return name +
                "(" +
                elements.mapNotNull { it.repr() }.joinToString(", ") +
                ")"
    }

    private fun sumOfNotNull(vararg values: String?) = listOfNotNull(*values).run {
        if (isEmpty()) "0" else joinToString(" + ")
    }

    fun reprOf(value: TAlignment): String = sumOfNotNull(value.horizontal?.value, value.vertical?.value)

    fun reprOf(value: TAngle): String = "${value.deg.value}deg"
    fun reprOf(value: TFraction): String = "${value.value.value}fr"
    fun reprOf(value: TRatio): String = "${value.value.value * 100}%"
    fun reprOf(value: TLength): String =
        sumOfNotNull(value.em?.let { "${it.repr()}em" }, value.pt?.let { "${it.repr()}pt" })

    fun reprOf(value: TRelative): String =
        listOfNotNull(
            value.abs?.em?.let { "${it.repr()}em" },
            value.abs?.pt?.let { "${it.repr()}pt" },
            value.rel?.repr()
        ).joinToString(" + ")

    fun reprOf(value: TPattern): String = structRepr(
        "pattern",
        ArgumentEntry(false, "size", value.size),
        ArgumentEntry(false, "spacing", value.spacing),
        ArgumentEntry(false, "relative", value.relative),
        ArgumentEntry(false, null, value.body ?: TSpace()),
    )

    fun reprOf(value: TCounter): String = when (value.value) {
        is TPageCounterKey -> "counter(page)"
        is TStrCounterKey -> "counter(${value.value.str.repr()})"
        is TSelectorCounterKey -> "counter(${value.value.selector.repr()})"
        else -> throw AssertionError()
    }

    fun reprOf(value: TRegexSelector): String = "selector(${value.regex.repr()})"

    fun reprOf(value: TLabelSelector): String = "selector(${value.label.repr()})"

    fun reprOf(value: TElementSelector): String =
        if (value.where == null) value.element.value
        else value.element.value +
                ".where(" +
                value.where.value.map { it.key + " : " + it.value.repr() }.joinToString(", ") +
                ")"

    fun reprOf(value: TAfterSelector): String {
        return "selector(" + value.repr() +
                ").after(" +
                value.start.repr() +
                (if (value.inclusive != null) ", inclusive: ${value.inclusive.repr()}" else "") +
                ")"
    }

    fun reprOf(value: TBeforeSelector): String {
        return "selector(" + value.repr() +
                ").before(" +
                value.end.repr() +
                (if (value.inclusive != null) ", inclusive: ${value.inclusive.repr()}" else "") +
                ")"
    }

    fun reprOf(value: TAndSelector): String = when (value.variants.size) {
        0 -> throw IllegalArgumentException()
        1 -> value.variants[0].repr()
        else -> "selector(" + value.variants[0].repr() + ").and(" +
                value.variants.drop(1).joinToString(", ") { it.repr() } + ")"
    }

    fun reprOf(value: TOrSelector): String = when (value.variants.size) {
        0 -> throw IllegalArgumentException()
        1 -> value.variants[0].repr()
        else -> "selector(" + value.variants[0].repr() + ").or(" +
                value.variants.drop(1).joinToString(", ") { it.repr() } + ")"
    }

    fun reprOf(value: TType): String = value.name
    fun reprOf(value: TModule): String = value.name.value
    fun reprOf(value: TDirection): String = value.value.value

    fun reprOf(value: TMathRoot): String = if (value.index == null) {
        structRepr("math.sqrt", ArgumentEntry(false, null, value.radicand))
    } else {
        structRepr("math.root", ArgumentEntry(false, null, value.index), ArgumentEntry(false, null, value.radicand))
    }

    fun reprOf(value: TMathAlignPoint): String = "$ & $.body"

    fun reprOf(value: TStyled): String {
        return "{ " + value.styles.value.joinToString("; ") { it.repr() } + "; ${value.child.repr()}; }"
    }

    fun reprOf(value: TFunction): String = when (value) {
        is TElement -> reprOf(value)
        is TWith -> "(" + value.origin.repr() + ").with(.." + value.args.repr() + ")"
        is TNativeFunc -> value.name.value
        is TClosure -> StringBuilder().apply {
            for ((name, v) in value.captured) {
                append("let $name = ")
                append(v.repr())
                append("; ")
            }
            append(value.node) // TODO I guess
        }.toString()

        else -> throw AssertionError(value)
    }

    fun elementRepr(s: String, vararg entries: ArgumentEntry): String = structRepr(s, *entries) // TODO

    //    fun reprOf(rule: TSetRule): String = "set ${rule.elem.value}(${rule.id.value}: ${rule.value.repr()})"
    fun setRepr(s: String, vararg entries: ArgumentEntry): String = "set " + elementRepr(s, *entries) // TODO

    fun reprOf(rule: TShowRule): String {
        val start = StringBuilder().apply {
            append("show")
            if (rule.selector is TSelector) {
                append(" ")
                append(rule.selector.repr())
            }
            append(": ")
        }.toString()

        return if (rule.transform is TArray<*>) {
            rule.transform.joinToString("; ") {
                start + it.repr()
            }
        } else {
            start + rule.transform.repr()
        }
    }

    fun reprOf(context: TContext) = "context " + context.func.repr()

    fun reprOf(context: TStyleDeprecated) = "style(" + context.func.repr() + ")"
    fun reprOf(update: TCounterUpdate): String =
        "counter(" + update.key.repr() + ").update(" + update.update.repr() + ")" // TODO something wrong is going on with counter here

    fun reprOf(step: TCounterStep): String =
        "counter(" + step.key.repr() + ").step(level:" + step.level.repr() + ")" // TODO something wrong is going on with counter here

    fun reprOf(update: TStateUpdate): String = "state(" + update.key.repr() + ").update(" + update.update.repr() + ")"
}