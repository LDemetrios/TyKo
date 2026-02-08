package org.ldemetrios.tyko.model


import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.hasAnnotation


sealed class TContent() : SequenceElement(), TValue, TTransform, ArrayOrSingle<TContent>, Option<TContent>,
    Smart<TContent>, Attribution, Computable<TContent>, TStackComponent, TAttachment {
    override fun type(): TType = TYPE
    abstract fun elem(): TElement
    abstract val label: TLabel?
    companion object {
        val TYPE = TType.CONTENT
    }

    internal open fun reprImpl(sb: StringBuilder) {
        elem().repr(sb)
        sb.append("(")
        writeFieldsInto(sb)
        sb.append(")")
    }

    override fun repr(sb: StringBuilder)  {
        if (label == null) {
            reprImpl(sb)
        } else {
            sb.append("[#(")
            reprImpl(sb)
            sb.append(")#")
            label!!.repr(sb)
            sb.append("]")
        }
    }
}

@SerialName("str")
enum class TFillRule(val value: String) : IntoStr {
    @SerialName("non-zero")
    NON_ZERO("non-zero"),
    @SerialName("even-odd")
    EVEN_ODD("even-odd");

    override fun intoValue(): TStr = TStr(value)

    companion object {
        fun fromValue(value: TValue) = when (value) {
            NON_ZERO.value.t -> NON_ZERO
            EVEN_ODD.value.t -> EVEN_ODD
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}


sealed class TSelectableContent<Self: TSelectableContent<Self>>() : TContent() {
    abstract override fun elem(): TLocatableElement<Self>
}
