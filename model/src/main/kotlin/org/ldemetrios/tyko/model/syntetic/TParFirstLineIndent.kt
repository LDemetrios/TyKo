package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.SerialName
import org.ldemetrios.tyko.model.TBool
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TLength
import org.ldemetrios.tyko.model.TValue


sealed interface FirstLineIndent : IntoValue {
    val amount: TLength
    val all: TBool?

    companion object {
        fun fromValue(value: TValue): FirstLineIndent = when (value) {
            is TDict<*> -> FirstLineIndentImpl.fromValue(value)
            is TLength -> value
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@SerialName("dict")
data class FirstLineIndentImpl(
    override val amount: TLength,
    override val all: TBool? = null
) : FirstLineIndent, Self<FirstLineIndentImpl> {
    override fun intoValue(): TDict<IntoValue> = if (all != null) TDict(
        mapOf("amount" to amount, "all" to all)
    ) else TDict(
        mapOf("amount" to amount)
    )

    companion object {
        fun fromValue(value: TValue): FirstLineIndentImpl = when (value) {
            is TDict<*> -> FirstLineIndentImpl(
                value["amount"]!!.intoValue() as TLength,
                value["all"]?.intoValue()?.let { it as TBool }
            )

            else -> throw AssertionError("Can't convert from $value")
        }
    }
}
