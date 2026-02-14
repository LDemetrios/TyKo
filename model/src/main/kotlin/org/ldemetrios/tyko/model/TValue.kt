package org.ldemetrios.tyko.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.javaField

/**
 * Almost common interface for Typst values. Almost, because there's a wider hierarchy,
 * [IntoValue], which includes synthetic values, representing values with specific restrictions,
 * which can be used in certain places. However, any "generic" Typst value is TValue.
 */
@JsonClassDiscriminator("type")
sealed interface TValue : IntoValue {
    /**
     * The [TType] of the value, as in `type(value)` in Typst
     */
    fun type(): TType

    override fun intoValue(): TValue = this

    /**
     * Append a `repr` of the value onto given StringBuilder.
     * Warning: the resulting `repr` is not human-readable, but it is correct Typst code, representing this value.
     */
    fun repr(sb: StringBuilder)  {
        sb.append(javaClass.getAnnotation(SerialName::class.java).value)
        sb.append("(")
        writeFieldsInto(sb)
        sb.append(")")
    }
}
