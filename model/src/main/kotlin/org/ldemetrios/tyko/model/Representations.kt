package org.ldemetrios.tyko.model

import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField

internal fun IntoValue.repr(sb: StringBuilder, registrar: (Any) -> Pair<Long, Long>) = intoValue().repr(sb, registrar)

/**
 * `repr` of the value this object represents.
 * Warning: the resulting `repr` is not human-readable, but it is correct Typst code, representing this value.
 */
fun IntoValue.repr(registrar: (Any) -> Pair<Long, Long> = { error("Session is not present, can't register $it") }) =
    buildString { repr(this, registrar) }

internal fun StringBuilder.sumOfNotNull(registrar: (Any) -> Pair<Long, Long>, vararg value: IntoValue?) {
    this.sumOfNotNull(null as String?, registrar, *value)
}

internal fun StringBuilder.sumOfNotNull(
    ifEmpty: String?,
    registrar: (Any) -> Pair<Long, Long>,
    vararg value: IntoValue?
) {
    val values = value.filterNotNull()
    when (values.size) {
        0 -> append(ifEmpty!!)
        1 -> values[0].repr(this, registrar)
        else -> {
            append("(")
            values.forEachIndexed { index, value ->
                if (index > 0) {
                    append(" + ")
                }
                value.repr(this, registrar)
            }
            append(")")
        }
    }
}

internal fun TValue.writeFieldsInto(sb: StringBuilder, registrar: (Any) -> Pair<Long, Long>) {
    val kClass = this.javaClass.kotlin
    val props = kClass.declaredMembers
        .filterIsInstance<KProperty<*>>()
        .filter { it.name != "label" }
    val propsByName = props.associateBy { it.name }
    val positionalNames = kClass.primaryConstructor
        ?.parameters
        ?.mapNotNull { it.name }
        .orEmpty()
    val positionalProps = positionalNames.mapNotNull { propsByName[it] }
        .filter { it.javaField?.isAnnotationPresent(Positional::class.java) == true }
    val positionalSet = positionalProps.toSet()
    val namedProps = props.filter { it !in positionalSet }

    positionalProps.forEach { appendField(it, sb, registrar) }
    namedProps.forEach { appendField(it, sb, registrar) }
}

internal fun TValue.appendField(
    property: KProperty<*>,
    sb: StringBuilder,
    registrar: (Any) -> Pair<Long, Long>,
) {
    val value = property.getter.call(this) as IntoValue?
    val positional = property.javaField!!.isAnnotationPresent(Positional::class.java)
    val variadic = property.javaField!!.isAnnotationPresent(Variadic::class.java)
    if (value != null) {
        if (!positional) {
            val name = property.findAnnotation<SerialName>()?.value
                ?: property.javaField!!.getAnnotation(SerialName::class.java)?.value
                ?: camelToKebab(property.name)
            sb.append(name)
            sb.append(":")
        }
        if (variadic) {
            sb.append("..")
        }
        value.repr(sb, registrar)
        sb.append(",")
    }
}
