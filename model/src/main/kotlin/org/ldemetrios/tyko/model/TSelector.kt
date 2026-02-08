package org.ldemetrios.tyko.model


sealed interface TSelector<out S : TValue> : TCounterKey, TValue {
    override fun type(): TType = TYPE

    fun reprAsSelector(sb: StringBuilder) = repr(sb)

    companion object {
        val TYPE = TType.SELECTOR
    }
}

sealed interface TSelectable<out S : TValue> : TSelector<S> {
    override fun reprAsSelector(sb: StringBuilder) {
        sb.append("selector(")
        repr(sb)
        sb.append(")")
    }
}

@SerialName("selector.or")
data class TOrSelector<out S : TValue>(val options: TArray<TSelector<S>>) : TSelector<S>, Smart<TSelector<S>> {
    override fun repr(sb: StringBuilder) {
        options.first<TSelector<S>>().reprAsSelector(sb)
        if (options.size > 1) {
            sb.append(".or(")
            options.drop(1).forEach { it.reprAsSelector(sb); sb.append(",") }
            sb.append(")")
        }
    }
}


@SerialName("selector.and")
data class TAndSelector<S : TValue>(val options: TArray<TSelector<S>>) : TSelector<S>, Smart<TSelector<S>> {
    override fun repr(sb: StringBuilder) {
        options.first<TSelector<S>>().reprAsSelector(sb)
        if (options.size > 1) {
            sb.append(".and(")
            options.drop(1).forEach { it.reprAsSelector(sb); sb.append(",") }
            sb.append(")")
        }
    }
}


@SerialName("selector.before")
data class TBeforeSelector<S : TValue>(
    val selector: TSelector<S>,
    val end: TSelector<*>,
    val inclusive: Boolean = true
) : TSelector<S>, Smart<TSelector<S>> {
    override fun repr(sb: StringBuilder) {
        selector.reprAsSelector(sb)
        sb.append(".before(")
        end.repr(sb)
        if (!inclusive) {
            sb.append(", inclusive: false")
        }
        sb.append(")")
    }
}


@SerialName("selector.after")
data class TAfterSelector<S : TValue>(
    val selector: TSelector<S>,
    val start: TSelector<*>,
    val inclusive: Boolean = true
) : TSelector<S>, Smart<TSelector<S>> {
    override fun repr(sb: StringBuilder) {
        selector.reprAsSelector(sb)
        sb.append(".after(")
        start.repr(sb)
        if (!inclusive) {
            sb.append(", inclusive: false")
        }
        sb.append(")")
    }
}

@SerialName("selector.where")
data class TWhereSelector<S : TContent>( val func: TSelectableFunc<S>,  val args: TArgs<*>) : TSelector<S> {
    override fun repr(sb: StringBuilder) {
        func.repr(sb)
        sb.append(".where(..")
        args.repr(sb)
        sb.append(")")
    }
}
