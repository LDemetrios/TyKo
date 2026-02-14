package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.UnionType

sealed interface TSmartquoteQuotes : IntoValue {
    companion object {
        fun fromValue(value: TValue): TSmartquoteQuotes = when (value) {
            is TSmartquoteLevel -> value
            is TDict<*> -> TSmartquoteSymbols.fromValue(value)
            is TArray<*> -> TOpenCloseQuotes.fromValue(value)
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@UnionType(["str", "auto"])
sealed interface TSmartquoteLevel : TSmartquoteQuotes, TValue

@SerialName("array")
data class TOpenCloseQuotes(val open: TStr, val close: TStr) : TSmartquoteQuotes, IntoArr<TStr>, Self<TOpenCloseQuotes> {
    override fun intoValue(): TArray<TStr> = TArray(listOf(open, close))

    companion object {
        fun fromValue(value: TValue): TOpenCloseQuotes = when (value) {
            is TArray<*> -> if (value.size == 2) TOpenCloseQuotes(
                value[0].intoValue() as TStr,
                value[1].intoValue() as TStr
            ) else {
                throw AssertionError("Can't convert from $value")
            }

            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@SerialName("dict")
data class TSmartquoteSymbols(
    val single: TSmartquoteQuotes?,
    val double: TSmartquoteQuotes?,
) : IntoDict<TSmartquoteQuotes>, TSmartquoteQuotes, Self<TSmartquoteSymbols> {
    override fun intoValue(): TDict<TSmartquoteQuotes> = TDict(
        mapOfNotNullValues(
            "single" to single,
            "double" to double,
        )
    )

    companion object {
        fun fromValue(value: TValue): TSmartquoteSymbols = when (value) {
            is TDict<*> -> TSmartquoteSymbols(
                value["single"]?.intoValue()?.let { TSmartquoteQuotes.fromValue(it) },
                value["double"]?.intoValue()?.let { TSmartquoteQuotes.fromValue(it) },
            )

            else -> throw AssertionError("Can't convert from $value")
        }
    }
}
