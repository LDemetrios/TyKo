package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField

@UnionType(["style", "content", "symbol"])
sealed class SequenceElement : TValue


@SerialName("styles")
data class TStyles(@SerialName("value") val value: TArray<TStyle>) : TValue, TTransform {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.STYLE
    }

    override fun repr(sb: StringBuilder) {
        if (value.size == 1) {
            value[0].repr(sb)
        } else {
            val classes = value.map { it.javaClass }
            if (classes.distinct().size != 1) {
                throw AssertionError("Can't repr joined `styles` of different types")
            }
            sb.append("set ")

            val klass = classes[0]
            klass.getAnnotation(SerialName::class.java)
                .value
                .removePrefix("set-")
                .let(sb::append)
            sb.append("(")
            for (style in value) {
                klass.kotlin
                    .declaredMembers
                    .filterIsInstance<KProperty<*>>()
                    .filter { it.name != "internals" }
                    .forEach {
                        style.appendField(it, sb)
                    }
            }
            sb.append(")")
        }
    }
}


sealed class TStyle : SequenceElement(), TValue {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.STYLE
    }
}


@SerialName("show-rule")
data class TShowRule(
    val selector: TSelector<*>?,
    val transform: TTransform,
    val outside: Boolean? = null, // TODO wtf is this?
) : TStyle() {
    override fun repr(sb: StringBuilder) {
        sb.append("show")
        if (selector != null) {
            sb.append(" ")
            selector.repr(sb)
        }
        sb.append(":")
        transform.repr(sb)
    }
}


@SerialName("internals")
data class SetRuleInternals(
    val id: Int,
    val span: Long,
    val liftable: Boolean,
    val outside: Boolean
)


sealed class TSetRule() : TStyle() {
    abstract val internals: SetRuleInternals?
    override fun type(): TType = TYPE

    override fun repr(sb: StringBuilder) {
        sb.append("set ")
        this.javaClass
            .getAnnotation(SerialName::class.java)
            .value
            .removePrefix("set-")
            .let(sb::append)
        sb.append("(")
        this.javaClass.kotlin
            .declaredMembers
            .filterIsInstance<KProperty<*>>()
            .filter { it.name != "internals" }
            .forEach {
                appendField(it, sb)
            }
        sb.append(")")
    }
}
