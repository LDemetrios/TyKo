package org.ldemetrios.tyko.model

import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField

internal fun IntoValue.repr(sb: StringBuilder) = intoValue().repr(sb)

fun IntoValue.repr() = buildString { repr(this) }

fun StringBuilder.sumOfNotNull(vararg value: IntoValue?) {
    this.sumOfNotNull(null as String?, *value)
}

fun StringBuilder.sumOfNotNull(ifEmpty: String?, vararg value: IntoValue?) {
    val values = value.filterNotNull()
    when (values.size) {
        0 -> append(ifEmpty!!)
        1 -> values[0].repr(this)
        else -> {
            append("(")
            values.forEachIndexed { index, value ->
                if (index > 0) {
                    append(" + ")
                }
                value.repr(this)
            }
            append(")")
        }
    }
}

fun TValue.writeFieldsInto(sb: StringBuilder) {
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

    positionalProps.forEach { appendField(it, sb) }
    namedProps.forEach { appendField(it, sb) }
}

fun TValue.appendField(
    property: KProperty<*>,
    sb: StringBuilder
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
        value.repr(sb)
        sb.append(",")
    }
}
