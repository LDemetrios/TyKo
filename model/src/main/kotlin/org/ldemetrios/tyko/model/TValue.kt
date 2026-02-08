package org.ldemetrios.tyko.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.javaField


@JsonClassDiscriminator("type")
sealed interface TValue : IntoValue {
    fun type(): TType
    override fun intoValue(): TValue = this
    fun repr(sb: StringBuilder)  {
        sb.append(javaClass.getAnnotation(SerialName::class.java).value)
        sb.append("(")
        writeFieldsInto(sb)
        sb.append(")")
    }
}
