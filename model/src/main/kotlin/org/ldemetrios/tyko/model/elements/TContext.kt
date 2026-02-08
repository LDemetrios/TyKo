package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator


@SerialName("context")
data class TContext(
    @all:Positional val func: TClosure,
    override val label: TLabel? = null
) : TSelectableContent<TContext>() {
    override fun elem(): TLocatableElement<TContext> = ELEM

    companion object {
        val ELEM = TLocatableElement<TContext>("context")
    }

    override fun reprImpl(sb: StringBuilder) {
        sb.append("{")
        for ((k, v) in func.captured) {
            sb.append("let $k = ")
            v.repr(sb)
            sb.append(";")
        }
        sb.append("context (")
        sb.append((func.node as SyntClosure).value)
        sb.append(")")
        sb.append("}")
    }
}
