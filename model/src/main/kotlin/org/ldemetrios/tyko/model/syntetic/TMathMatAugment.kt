package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.ArrayOrSingle
import org.ldemetrios.tyko.model.IntoDict
import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.Option
import org.ldemetrios.tyko.model.SerialName
import org.ldemetrios.tyko.model.Smart
import org.ldemetrios.tyko.model.TAuto
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TInt
import org.ldemetrios.tyko.model.TStroke
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.mapOfNotNullValues


sealed interface TMatAugment: IntoValue {
    companion object {
        fun fromValue(value: TValue) : TMatAugment = when (value) {
            is TDict<*> -> TMatAugmentDict.fromValue(value)
            is TInt -> value
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@SerialName("dict")
data class TMatAugmentDict(
    val hline: ArrayOrSingle<TInt>? = null,
    val vline: ArrayOrSingle<TInt>? = null,
    val stroke: Smart<TStroke>? = null
) : IntoDict<IntoValue>, TMatAugment, Self<TMatAugmentDict> {
    override fun intoValue(): TDict<IntoValue> = TDict(
        mapOfNotNullValues(
            "hline" to hline,
            "vline" to vline,
            "stroke" to stroke.takeIf { it != TAuto }
        )
    )

    companion object {
        fun fromValue(value: TValue) = if(value is TDict<*>) TMatAugmentDict(
            value["hline"]?.intoValue()?.let { ArrayOrSingle.fromValue<TInt>(it) },
            value["vline"]?.intoValue()?.let { ArrayOrSingle.fromValue<TInt>(it) },
            value["stroke"]?.intoValue()?.let { Smart.fromValue<TStroke>(it) },
        ) else throw AssertionError("Can't convert from $value")
    }
}
