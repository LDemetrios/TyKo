package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.IntoStr
import org.ldemetrios.tyko.model.SerialName
import org.ldemetrios.tyko.model.Smart
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TValue


/**
 * Lists paint coordinate spaces for gradients and patterns.
 */
@SerialName("str")
enum class PaintRelativeTo : IntoStr, Self<PaintRelativeTo> {
    @SerialName("self")
    SELF,

    @SerialName("parent")
    PARENT;

    override fun intoValue(): TStr = TStr(name.lowercase())

    companion object {
        val valuesByStr by lazy { entries.associateBy { it.intoValue() } }
        fun fromValue(value: TValue): PaintRelativeTo = when (value) {
            is TStr -> valuesByStr[value]!!
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}