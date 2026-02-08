package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable
import org.ldemetrios.tyko.model.TElement


sealed interface TFunc : TValue, TTransform, Progression<Nothing>, TFigureKind, Numbering, Computable<Nothing>,
    Smart<TFunc>,
    Option<TFunc> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.FUNC
    }
}


sealed interface TSelectableFunc<S : TContent> : TFunc, TSelectable<S> {
    override fun type(): TType = TFunc.TYPE
}


@SerialName("native-function")
data class TNativeFunc(
    val name: String,
    val ptr: Long? = null,
) : TFunc {
    override fun equals(other: Any?): Boolean =
        this === other || other is TElement &&
                if (ptr == null || other.ptr == null) {
                    name == other.name
                } else {
                    ptr == other.ptr
                }

    override fun repr(sb: StringBuilder) {
        sb.append(name)
    }
}


@SerialName("element")
open class TElement(
    open val name: String,
    open val ptr: Long? = null,
) : TFunc {
    override fun hashCode(): Int = name.hashCode()
    override fun equals(other: Any?): Boolean =
        this === other || other is TElement &&
                if (ptr == null || other.ptr == null) {
                    name == other.name
                } else {
                    ptr == other.ptr
                }

    override fun repr(sb: StringBuilder) {
        sb.append(
            when (name) {
                "sequence" -> "[a\\ ].func()"
                "math.align-point" -> "\$&\$.body.func()"
                else -> name
            }
        )
    }
}

@SerialName("element")
class TLocatableElement<S : TContent>(
    name: String,
    ptr: Long? = null,
) : TElement(name, ptr), TSelectableFunc<S>


@SerialName("closure")
data class TClosure(
    val node: TClosureSyntaxNode,
    val defaults: TArray<TValue>,
    val captured: TDict<TValue>,
    val numPosParams: Long,
) : TFunc {
    override fun repr(sb: StringBuilder) {
        sb.append("{")
        for ((k, v) in captured) {
            sb.append("let $k = ")
            v.repr(sb)
            sb.append(";")
        }
        if (node is RealClosure) {
            val name = node.name
            if (name != null) {
                sb.append("{ let ")
                sb.append(node.value)
                sb.append("; ")
                sb.append(name)
                sb.append("}")
            } else {
                sb.append(node.value)
            }
        } else {
            sb.append("(context (")
            sb.append(node.value)
            sb.append(")).func")
        }
        sb.append("}")
    }
}


sealed class TClosureSyntaxNode {
    abstract val value: String
}

@SerialName("real-closure")
data class RealClosure(
    override val value: String,
    val name: String? = null,
) : TClosureSyntaxNode()


@SerialName("synt-closure")
data class SyntClosure(override val value: String) : TClosureSyntaxNode()

@SerialName("func.with")
data class TFuncWith(val func: TFunc, val args: TArgs<*>) : TFunc {
    override fun repr(sb: StringBuilder) {
        sb.append("(")
        func.repr(sb)
        sb.append(".with(..")
        args.repr(sb)
        sb.append("))")
    }
}
