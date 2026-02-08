package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable
import kotlin.collections.mapNotNull
import kotlin.collections.orEmpty
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField


//!https://typst.app/docs/reference/math/roots/#functions-root
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A general root.
 * 
 * 
 */
@SerialName("math.root")
data class TMathRoot(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Which root of the radicand to take.
     * 
     * Positional, settable; Typst type: none|content
     */
    @all:Positional @all:Settable val index: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * The expression to take the root of.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val radicand: TContent,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.root")
    }

    override fun reprImpl(sb: StringBuilder) {
        sb.append("math.root(")
        (index ?: TNone).repr(sb)
        sb.append(",")
        radicand.repr(sb)
        sb.append(")")
    }
}


@SerialName("set-math.root")
data class TSetMathRoot(
    override val internals: SetRuleInternals? = null,
    val index: Option<TContent>? = null
) : TSetRule()
