package org.ldemetrios.tyko.model


sealed class TSymbol : TValue, TSymbolLike, ArrayOrSingle<TSymbol>, Option<TSymbol> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.SYMBOL
    }
}


@SerialName("symbol.single")
data class TSingleSymbol(val text: TStr) : TSymbol() {
    override fun repr(sb: StringBuilder) {
        sb.append("symbol(")
        text.repr(sb)
        sb.append(")")
    }
}


@SerialName("symbol.complex")
data class TComplexSymbol(val value: TArray<SymVariant>) : TSymbol() {
    override fun repr(sb: StringBuilder) {
        sb.append("symbol(..")
        value.repr(sb)
        sb.append(")")
    }
}


@SerialName("symbol.modified")
data class TModifiedSymbol(val list: TArray<SymVariant>, val modifiers: String) : TSymbol() {
    override fun repr(sb: StringBuilder) {
        TComplexSymbol(list).repr(sb)
        sb.append(".").append(modifiers)
    }
}

@SerialName("array")
data class SymVariant(val variant: TStr, val value: TStr) : IntoArr<TStr> {
    override fun intoValue(): TArray<TStr> {
        return TArray(listOf(variant, value))
    }

    companion object {
        fun fromValue(value: TValue) = if (value is TArray<*> && value.size == 2) {
            SymVariant(
                (value[0].intoValue() as TStr),
                (value[1].intoValue() as TStr),
            )
        } else throw AssertionError("Can't convert from $value")
    }
}
